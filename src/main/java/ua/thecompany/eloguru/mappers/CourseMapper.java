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

    CourseInitDto courseDtoToCourseInitDto(CourseDto courseInitDto);

    @Mapping(target = "teacherAccountId", source = "teacher")
    CourseDto courseModelToCourseDto(Course course);

    Course courseInitDtoToCourseModel(CourseInitDto courseInitDto);

//    Course updateCourseModelViaCourseInitDto(CourseInitDto courseInitDto, @MappingTarget Course course);

}
