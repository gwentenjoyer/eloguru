package ua.thecompany.eloguru.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.model.Course;

import java.io.IOException;
import java.util.List;

@Service
public interface CourseService {
    void createCourse(CourseInitDto courseInitDto, Long teacherId) throws IOException;

    Page<CourseDto> getCourses(@PageableDefault Pageable pageable);

    Page<CourseDto> searchCourseByHeader(@PageableDefault Pageable pageable, String header);

    CourseDto getCourseById(Long id);

    CourseDto updateCourse(CourseInitDto courseInitDto, Long id);

    void deleteCourseById(Long id);

    CourseDto addTopic(CourseDto courseDto, TopicInitDto topicInitDto);

    void force_delete();

    void enrollToCourse(Long courseId, Long studentAccountId);
    void disenrollFromCourse(Long courseId, Long studentAccountId);

    Long getCourseId(Course course);

    Course getCourseModel(Long id);

    boolean isAccountOwnsCourse(Long courseId, Long accountId);
    boolean isTeacherOwnsCourse(Long courseId, Long teacherId);

    List<Long> getCourseTopicsIds(Long courseId);

}
