package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import ua.thecompany.eloguru.dto.ContentDto;
import ua.thecompany.eloguru.dto.InitDto.ContentInitDto;
import ua.thecompany.eloguru.model.ContentObject;

import javax.swing.text.AbstractDocument;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    ContentObject contentInitDtoToContentModel(ContentInitDto contentInitDto);

    ContentDto contentModelToContentDto(ContentObject contentObject);

    ContentObject contentDtoToContentModel(ContentDto contentDto);
}
