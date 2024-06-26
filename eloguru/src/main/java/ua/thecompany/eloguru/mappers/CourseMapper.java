package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.model.Course;
import ua.thecompany.eloguru.services.TeacherService;
import ua.thecompany.eloguru.services.TopicService;

@Mapper(componentModel = "spring", uses = {TopicMapper.class, TeacherService.class, FeedbackMapper.class, TopicMapper.class, TopicService.class})
public interface CourseMapper {


    @Mapping(target = "teacher.id", source = "teacherId")
    Course courseDtoToCourseModel(CourseDto courseInitDto);

//    CourseInitDto courseDtoToCourseInitDto(CourseDto courseInitDto);

    @Mapping(target = "photo", ignore = true)
    CourseDto courseInitDtoToCourseDto(CourseInitDto     courseInitDto);

    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "topics", source = "topics", qualifiedByName = "topicToId")
    CourseDto courseModelToCourseDto(Course course);

    @Mapping(target = "photo", ignore = true)
    Course courseInitDtoToCourseModel(CourseInitDto courseInitDto);

//    Course updateCourseModelViaCourseInitDto(CourseInitDto courseInitDto, @MappingTarget Course course);
    @Named("courseToId")
    default Long courseToId(Course course) {
        if (!course.isActive())
            return -1l;
        return course.getId();
}
}
