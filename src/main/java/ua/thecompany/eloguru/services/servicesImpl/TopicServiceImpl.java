package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.mappers.TopicMapper;
import ua.thecompany.eloguru.model.Topic;
import ua.thecompany.eloguru.repositories.TopicRepository;
import ua.thecompany.eloguru.services.TopicService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private TopicRepository topicRepository;
    private TopicMapper topicMapper;

    @Override
    @Transactional
    public TopicDto createTopic(TopicInitDto topicInitDto) {
        log.info("Creating new topic named: " + topicInitDto.label());
        return topicMapper.topicModelToTopicDto(topicRepository.save(topicMapper.topicInitDtoToTopicModel(topicInitDto)));
    }

    @Override
    @Transactional
    public List<TopicDto> getTopics() {
        log.info("Retrieving all topics");
        return topicRepository.findByActive(true).stream().map(entity -> topicMapper.topicModelToTopicDto(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TopicDto> getTopicsByCourse(Long courseId) {
        log.info("Retrieving all topics");
        return topicRepository.findByActiveAndCourseId(true, courseId).stream().map(entity -> topicMapper.topicModelToTopicDto(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TopicDto getTopicById(Long id) {
        log.info("Retrieving topic by id: " + id);
        return topicMapper.topicModelToTopicDto(topicRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found topic with id: " + id)));
    }

    @Override
    @Transactional
    public Topic getTopicModelById(Long id) {
        log.info("Retrieving topic by id: " + id);
        return topicRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found topic with id: " + id));
    }

    @Override
    @Transactional
    public TopicDto updateTopic(TopicInitDto topicInitDto, Long id) {
        log.info("Updating topic by id: " + id);
        Topic topic = topicRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found topic with id: " + id));
        topic.setLabel(topicInitDto.label());
        return topicMapper.topicModelToTopicDto(topicRepository.save(topic));
    }

    @Override
    @Transactional
    public void deleteTopicById(Long id) {
        log.info("Deleting topic with id: " + id);
        topicRepository.deleteById(id);
    }
}
