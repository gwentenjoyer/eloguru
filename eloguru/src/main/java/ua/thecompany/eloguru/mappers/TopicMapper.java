package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.model.Course;
import ua.thecompany.eloguru.model.Topic;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic topicDtoToTopicModel(TopicDto topicDto);

    @Mapping(target = "topicId", source = "id")
    TopicDto topicModelToTopicDto(Topic topic);

    @Mapping(target = "course.id", source = "courseId")
    Topic topicInitDtoToTopicModel(TopicInitDto topicInitDto);

    @Mapping(target = "courseId", source = "course.id")
    TopicInitDto topicModelToTopicInitDto(Topic topic);

    TopicInitDto topicDtoToTopicInitDto(TopicDto topicDto);


    @Named("topicToId")
    default Long topicToId(Topic topic) {
        return topic.getId();
    }
}
