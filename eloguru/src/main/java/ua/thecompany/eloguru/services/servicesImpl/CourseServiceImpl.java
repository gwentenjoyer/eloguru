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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.StudentCourseProgressDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.mappers.CourseMapper;
import ua.thecompany.eloguru.mappers.StudentCourseProgressMapper;
import ua.thecompany.eloguru.mappers.TopicMapper;
import ua.thecompany.eloguru.model.*;
import ua.thecompany.eloguru.repositories.CourseRepository;
import ua.thecompany.eloguru.repositories.StudentCourseProgressRepository;
import ua.thecompany.eloguru.repositories.StudentRepository;
import ua.thecompany.eloguru.services.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final StudentRepository studentRepository;
    private final TeacherService teacherService;

    private CourseRepository courseRepository;
    private StudentCourseProgressRepository studentCourseProgressRepository;
    private CourseMapper courseMapper;
    private final TopicMapper topicMapper;
    private final TopicService topicService;
    private final FeedbackService feedbackService;
    private final AccountService accountService;
    private final StudentCourseProgressMapper studentCourseProgressMapper;

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
    public Page<CourseDto> getCourses(Pageable pageable, String sortBy, String order) {
        Page<Course> coursesPage;
        if ("rating".equalsIgnoreCase(sortBy)) {
            coursesPage = "desc".equalsIgnoreCase(order) ?
                    courseRepository.findAllByOrderByRatingDesc(pageable) :
                    courseRepository.findAllByOrderByRatingAsc(pageable);
        } else if ("durationDays".equalsIgnoreCase(sortBy)) {
            coursesPage = "desc".equalsIgnoreCase(order) ?
                    courseRepository.findAllByOrderByDurationDaysDesc(pageable) :
                    courseRepository.findAllByOrderByDurationDaysAsc(pageable);
        } else {
            coursesPage = courseRepository.findAll(pageable);
        }
        return coursesPage.map(courseMapper::courseModelToCourseDto);
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
    public TopicDto addTopic(Long courseId, TopicInitDto topicInitDto){
        Course course = courseRepository.findByIdAndActive(courseId, true).orElseThrow(() -> new EntityNotFoundException("Could not find course with id: " + courseId));
        if (course.getTopics() == null) {
            course.setTopics(new ArrayList<>());
        }
        Topic topic = topicService.createTopic(topicInitDto);
        course.getTopics().add(topic);
        return topicMapper.topicModelToTopicDto(topic);
    }

    @Override
    @Transactional
    public ResponseEntity<StudentCourseProgressDto> getCourseProgress(Principal principal, @PathVariable Long courseId) {
        if (principal == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            var progress = studentCourseProgressRepository.findByStudentIdAndCourseId(accountService.getStudentByAccountId(
                            accountService.getIdByEmail(principal.getName())).id(), courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find progress for student"));
            return new ResponseEntity<>(studentCourseProgressMapper.toDTO(progress), HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void force_delete() {
        courseRepository.deleteByActiveFalse();
    }

    @Override
    @Transactional
    public void enrollToCourse(Long courseId, Long studentAccountId) throws EntityNotFoundException {
        Student student = studentRepository.findByIdAndActive(studentAccountId, true).orElseThrow(() -> new EntityNotFoundException("Could not find student with id: " + studentAccountId));
        Course course = courseRepository.findByIdAndActive(courseId, true).orElseThrow(() -> new EntityNotFoundException("Could not find course with id: " + courseId));
        if (course.getStudents().contains(student)){
            log.info("Student " + student.getId() + " already enrolled to course " + courseId);
            return;
        }
        course.getStudents().add(student);
        courseRepository.save(course);
        log.info("Added student with id: " + studentAccountId + " to course with id: " + courseId);
        StudentCourseProgress studentCourseProgress = StudentCourseProgress.builder()
                .student(student)
                .course(course)
                .isCompleted(false)
                .progressPercentage(0.0)
                .build();
        studentCourseProgressRepository.save(studentCourseProgress);
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
    public ResponseEntity<Boolean> checkEnrollToCourse(Principal principal, Long courseId) {
        try{
            if (principal == null)
                throw new AuthenticationException("Empty principal");
            if (accountService.getUserRole(principal.getName()) != EnumeratedRole.STUDENT.toString())
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<Boolean>(courseRepository.isUserEnrolledInCourse(courseId,
                    accountService.getStudentByAccountId(accountService.getIdByEmail(principal.getName())).id()), HttpStatus.OK);
        }
        catch(AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
