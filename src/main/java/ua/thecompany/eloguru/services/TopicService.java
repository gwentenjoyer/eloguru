package ua.thecompany.eloguru.services;

import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.model.Topic;

import java.util.List;

@Service
public interface TopicService {
    TopicDto createTopic(TopicInitDto topicInitDto);

    List<TopicDto> getTopics();

    TopicDto getTopicById(Long id);

    Topic getTopicModelById(Long id);

    TopicDto updateTopic(TopicInitDto topicInitDto, Long id);

    void deleteTopicById(Long id);
}
