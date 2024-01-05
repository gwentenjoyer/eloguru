package ua.thecompany.eloguru.services.servicesImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.ContentDto;
import ua.thecompany.eloguru.dto.InitDto.ContentInitDto;
import ua.thecompany.eloguru.mappers.ContentMapper;
import ua.thecompany.eloguru.repositories.ContentRepository;
import ua.thecompany.eloguru.services.ContentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ContentServiceImpl implements ContentService {
    private ContentRepository contentRepository;
    private ContentMapper contentMapper;

    @Override
    @Transactional
    public void saveContent(ContentInitDto contentInitDto) {
        log.info("Content with id " + (contentRepository
                .save(contentMapper.contentInitDtoToContentModel(contentInitDto))).getId() + " successfully saved.");
    }

    @Override
    @Transactional
    public List<ContentDto> getContents(Long topicId) {
        log.info("Retrieving all contents of topic with id " + topicId);
        return contentRepository.findByTopic(topicId).stream().map(entity -> contentMapper.contentModelToContentDto(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContentDto getContentById(Long id) {

    }

    @Override
    @Transactional
    public ContentDto updateContent(Long contentId, ContentInitDto contentInitDto) {

    }

    @Override
    @Transactional
    public void deleteContentById(Long id) {

    }

    @Override
    @Transactional
    public boolean isAccountOwnsCourse(Long courseId, Long accountId) {

    }

}
