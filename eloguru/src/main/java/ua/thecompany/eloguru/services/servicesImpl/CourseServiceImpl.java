package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.mappers.CourseMapper;
import ua.thecompany.eloguru.mappers.TopicMapper;
import ua.thecompany.eloguru.model.Course;
import ua.thecompany.eloguru.repositories.CourseRepository;
import ua.thecompany.eloguru.repositories.StudentRepository;
import ua.thecompany.eloguru.services.CourseService;
import ua.thecompany.eloguru.services.FeedbackService;
import ua.thecompany.eloguru.services.TeacherService;
import ua.thecompany.eloguru.services.TopicService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final StudentRepository studentRepository;
    private final TeacherService teacherService;

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private final TopicMapper topicMapper;
    private final TopicService topicService;
    private final FeedbackService feedbackService;

    @Override
    @Transactional
    public void createCourse(CourseInitDto courseInitDto, Long teacherAccountId) throws IOException {
        Course course = courseMapper.courseInitDtoToCourseModel(courseInitDto);
        MultipartFile photo = courseInitDto.photo();
        if (photo != null){
            String photoPath = savePhoto(photo);
            course.setPhoto(photoPath);
        }
        course.setTeacher(teacherService.getTeacherModelById(teacherAccountId));
        var res = courseRepository.save(course);
        log.info("Created new course with id: " + res.getId());
    }

    @Override
    @Transactional
    public Page<CourseDto> getCourses(@PageableDefault Pageable pageable) {
        log.info("Retrieving all courses");
        return courseRepository.findAll(pageable).map(courseMapper::courseModelToCourseDto);
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "courseCache", key = "#header")
    public Page<CourseDto> searchCourseByHeader(@PageableDefault Pageable pageable, String header) {
        log.info("Retrieving courses with pattern: " + header);
        return courseRepository.findByHeaderContainingIgnoreCase(pageable, header).map(courseMapper::courseModelToCourseDto);
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "courseCache", key = "#id")
    public CourseDto getCourseById(Long id) throws EntityNotFoundException{
        log.info("Retrieving course with id: " + id);
        return courseMapper.courseModelToCourseDto(courseRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found course with id " + id)));
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "courseCache", key = "#id")
    public CourseDto updateCourse(CourseInitDto courseInitDto, Long id) throws EntityNotFoundException {
        log.info("Updating course data with id: " + id);

        Optional<Course> retrievedCourse = courseRepository.findByIdAndActive(id, true);
        if (retrievedCourse.isPresent()) {
            Course course = retrievedCourse.get();
            Course res = courseRepository.save(updateCourseFromCourseDto(courseMapper.courseInitDtoToCourseDto(courseInitDto), course));
            ArrayList<FeedbackDto> feedbackDtos = (ArrayList<FeedbackDto>) feedbackService.getFeedbacks(id);
            if (!feedbackDtos.isEmpty())
                res.setRating(String.valueOf(feedbackDtos.stream()
                        .mapToDouble(FeedbackDto::rating)
                        .sum() / feedbackDtos.size()));
            return courseMapper.courseModelToCourseDto(res);
        } else {
            throw new EntityNotFoundException("Can't find course to update");
        }
    }

    @Transactional
    @CachePut(cacheNames = "courseCache", key = "#id")
    public CourseDto updateCourse(CourseDto courseDto, Long id) throws EntityNotFoundException{
        log.info("Updating course data with id: " + id);

        Optional<Course> retrievedCourse = courseRepository.findByIdAndActive(id, true);
        if (retrievedCourse.isPresent()) {
            Course course = retrievedCourse.get();

            return courseMapper.courseModelToCourseDto(courseRepository.save(updateCourseFromCourseDto(courseDto, course)));
        } else {
            throw new EntityNotFoundException();
        }
    }


    @Override
    @Transactional
    @CacheEvict(cacheNames = "courseCache", key = "#id")
    public void deleteCourseById(Long id) {
        log.info("Deleting course with id: " + id);
        courseRepository.deleteById(id);
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        Resource resource = new FileSystemResource("target/classes/static/coursesPhotos");
////        Resource resource = new FileSystemResource("static/coursesPhotos");
        try {
            String photoOriginalName = UUID.randomUUID().toString() + photo.getOriginalFilename();
            if (photoOriginalName == null) {
                throw new IOException("File name is invalid");
            }

            Path photoFolderPath = Paths.get(resource.getFile().getAbsolutePath());
            if (!Files.exists(photoFolderPath)) {
                Files.createDirectories(photoFolderPath);
            }

            Path destinationFilePath = photoFolderPath.resolve(photoOriginalName);

            Files.copy(photo.getInputStream(), destinationFilePath);
            return photoOriginalName;
        } catch (IOException e) {
            // Log the error
            System.err.println("Error saving photo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public CourseDto addTopic(CourseDto courseDto, TopicInitDto topicInitDto) {
        Course course = courseMapper.courseDtoToCourseModel(courseDto);
        if (course.getTopics() == null) {
            course.setTopics(new ArrayList<>());
        }
        course.getTopics().add(topicMapper.topicDtoToTopicModel(topicService.createTopic(topicInitDto)));
        return updateCourse(courseMapper.courseModelToCourseDto(course), courseDto.id());
    }

    @Override
    @Transactional
    public void force_delete() {
        courseRepository.deleteByActiveFalse();
    }

    @Override
    @Transactional
    public void enrollToCourse(Long courseId, Long studentAccountId) throws EntityNotFoundException {
        Course course = courseRepository.findByIdAndActive(courseId, true).orElseThrow(() -> new EntityNotFoundException("Could not find course with id: " + courseId));
        course.getStudents().add(studentRepository.findByIdAndActive(studentAccountId, true).orElseThrow(() -> new EntityNotFoundException("Could not find student with id: " + studentAccountId)));
        courseRepository.save(course);
        log.info("Added student with id: " + studentAccountId + " to course with id: " + courseId);
    }
    @Override
    @Transactional
    public void disenrollFromCourse(Long courseId, Long studentAccountId) {
        Course course = courseRepository.findByIdAndActive(courseId, true).orElseThrow(() -> new EntityNotFoundException("Could not find course with id: " + courseId));
        course.getStudents().remove(studentRepository.findByIdAndActive(studentAccountId, true).orElseThrow(() -> new EntityNotFoundException("Could not find student with id: " + studentAccountId)));
        courseRepository.save(course);
        log.info("Disrolled student with id: " + studentAccountId + " from course with id: " + courseId);
    }


    @Override
    @Transactional
//    @Cacheable
    public Long getCourseId(Course course){
        return course.getId();
    }

    @Override
    @Transactional
//    @Cacheable
    public Course getCourseModel(Long id) throws EntityNotFoundException {
        return courseRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found course with id " + id));
    }

    private Course updateCourseFromCourseDto(CourseDto courseDto, Course course) {
        if (courseDto.header() != null)
            course.setHeader(courseDto.header());
        if (courseDto.description() != null)
            course.setDescription(courseDto.description());
        if (courseDto.durationDays() != null)
            course.setDurationDays(courseDto.durationDays());
        if (courseDto.startDate() != null)
            course.setStartDate(courseDto.startDate());
        if (courseDto.categories() != null)
            course.setCategories(courseDto.categories());
        return course;
    }


    @Override
    @Transactional
    public boolean isAccountOwnsCourse(Long courseId, Long accountId) {
        return courseRepository.findByIdAndActive(courseId, true).orElseThrow(EntityNotFoundException::new)
                .getTeacher().getAccount().getId() == accountId;
    }

    @Override
    @Transactional
    public boolean isTeacherOwnsCourse(Long courseId, Long teacherId) {
        return courseRepository.findByIdAndActive(courseId, true).orElseThrow(EntityNotFoundException::new)
                .getTeacher().getId() == teacherId;
    }
    @Override
    @Transactional
    public List<Long> getCourseTopicsIds(Long courseId){
        System.out.println(courseRepository.selectTopicsByCourseId(courseId));
        return new ArrayList<Long>(courseRepository.selectTopicsByCourseId(courseId));
    }


}