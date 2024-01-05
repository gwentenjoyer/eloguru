package ua.thecompany.eloguru.services;

import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.ContentDto;
import ua.thecompany.eloguru.dto.InitDto.ContentInitDto;

import java.util.List;

@Service
public interface ContentService {
    void saveContent(ContentInitDto contentInitDto);

    List<ContentDto> getContents(Long topicId);

    ContentDto getContentById(Long id);

    ContentDto updateContent(Long contentId, ContentInitDto contentInitDto);

    void deleteContentById(Long id);

    boolean isAccountOwnsCourse(Long courseId, Long accountId);
}
