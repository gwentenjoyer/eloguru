package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.model.Course;
import ua.thecompany.eloguru.services.TeacherService;

@Mapper(componentModel = "spring", uses = {TopicMapper.class, TeacherService.class, FeedbackMapper.class})
public interface CourseMapper {


    @Mapping(target = "teacher", source = "teacherAccountId")
    Course courseDtoToCourseModel(CourseDto courseInitDto);

    @Mapping(target = "teacherAccountId", source = "teacher")
//    @Mapping(target = "feedbacks.studentAccountId", source = "feedbacks.owner")
//    @Mapping(target = "feedbacks.feedbackId", source = "feedbacks")
//    @Mapping(target = "feedbacks.courseId", source = "feedbacks.course")
//    @Mapping(target = "durationDays", source = "durationDays")
    CourseDto courseModelToCourseDto(Course course);

//    @Mapping(target = "teacher", source = "teacherAccountId")
    Course courseInitDtoToCourseModel(CourseInitDto courseInitDto);

//    @Mapping(target = "teacher", source = "teacherAccountId")
    Course updateCourseModelViaCourseInitDto(CourseInitDto courseInitDto, @MappingTarget Course course);

}
