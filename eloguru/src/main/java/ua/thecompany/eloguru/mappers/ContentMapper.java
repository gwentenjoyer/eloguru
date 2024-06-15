package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.thecompany.eloguru.dto.ContentDto;
import ua.thecompany.eloguru.dto.InitDto.ContentInitDto;
import ua.thecompany.eloguru.model.ContentObject;
import ua.thecompany.eloguru.services.TopicService;

import javax.swing.text.AbstractDocument;

@Mapper(componentModel = "spring", uses = {TopicService.class})
public interface ContentMapper {

    @Mapping(target = "topic", source = "topicId")
    ContentObject contentInitDtoToContentModel(ContentInitDto contentInitDto);

    @Mapping(target = "topicId", source = "topic.id")
    ContentDto contentModelToContentDto(ContentObject contentObject);

    ContentObject contentDtoToContentModel(ContentDto contentDto);
}
