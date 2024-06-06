package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.model.Course;
import ua.thecompany.eloguru.services.TeacherService;

@Mapper(componentModel = "spring", uses = {TopicMapper.class, TeacherService.class, FeedbackMapper.class})
public interface CourseMapper {


    @Mapping(target = "teacher.id", source = "teacherId")
    Course courseDtoToCourseModel(CourseDto courseInitDto);

    CourseInitDto courseDtoToCourseInitDto(CourseDto courseInitDto);

    @Mapping(target = "teacherId", source = "teacher.id")
    CourseDto courseModelToCourseDto(Course course);

    Course courseInitDtoToCourseModel(CourseInitDto courseInitDto);

//    Course updateCourseModelViaCourseInitDto(CourseInitDto courseInitDto, @MappingTarget Course course);
    @Named("courseToId")
    default Long courseToId(Course course) {
        return course.getId();
}
}
