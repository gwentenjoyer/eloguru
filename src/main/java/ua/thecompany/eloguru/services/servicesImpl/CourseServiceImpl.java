package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.Optional;

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
    public void createCourse(CourseInitDto courseInitDto, Long teacherAccountId) {
        Course course = courseMapper.courseInitDtoToCourseModel(courseInitDto);
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
            Course res = courseRepository.save(updateCourseFromCourseInitDto(courseInitDto, course));
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

            return courseMapper.courseModelToCourseDto(courseRepository.save(updateCourseFromCourseInitDto(courseMapper.courseDtoToCourseInitDto(courseDto), course)));
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

    private Course updateCourseFromCourseInitDto(CourseInitDto courseInitDto, Course course) {
        if (courseInitDto.header() != null)
            course.setHeader(courseInitDto.header());
        if (courseInitDto.description() != null)
            course.setDescription(courseInitDto.description());
        if (courseInitDto.durationDays() != null)
            course.setDurationDays(courseInitDto.durationDays());
        if (courseInitDto.startDate() != null)
            course.setStartDate(courseInitDto.startDate());
        if (courseInitDto.categories() != null)
            course.setCategories(courseInitDto.categories());
        return course;
    }


    @Override
    @Transactional
    public boolean isAccountOwnsCourse(Long courseId, Long accountId) {
        System.out.println(courseRepository.findByIdAndActive(courseId, true).orElseThrow(EntityNotFoundException::new)
                .getTeacher().getAccount().getId());

        return courseRepository.findByIdAndActive(courseId, true).orElseThrow(EntityNotFoundException::new)
                .getTeacher().getAccount().getId() == accountId;
    }

    @Override
    @Transactional
    public boolean isTeacherOwnsCourse(Long courseId, Long teacherId) {
        System.out.println(courseRepository.findByIdAndActive(courseId, true).orElseThrow(EntityNotFoundException::new)
                .getTeacher().getAccount().getId());

        return courseRepository.findByIdAndActive(courseId, true).orElseThrow(EntityNotFoundException::new)
                .getTeacher().getId() == teacherId;
    }

}
