package ua.thecompany.eloguru.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.FeedbackInitDto;
import ua.thecompany.eloguru.model.Feedback;
import ua.thecompany.eloguru.services.CourseService;

@Mapper(componentModel = "spring", uses = {CourseService.class})
public interface FeedbackMapper {
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "studentId", source = "owner.id")
    @Mapping(target = "feedbackId", source = "id")
    FeedbackDto feedbackModelToFeedbackDto(Feedback feedback);

    Feedback feedbackDtoToFeedbackModel(FeedbackDto feedbackDto);

    @Mapping(target = "course.id", source = "courseId")
//    @Mapping(target = "owner.id", source = "studentAccountId")
//    @Mapping(target = "id", source = "feedbackId")
    Feedback feedbackInitDtoToFeedbackModel(FeedbackInitDto feedbackInitDto);
}
