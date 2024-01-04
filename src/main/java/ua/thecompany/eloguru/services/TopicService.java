package ua.thecompany.eloguru.services;

import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.TopicDto;

import java.util.List;

@Service
public interface TopicService {
    TopicDto createTopic(TopicInitDto topicInitDto);

    List<TopicDto> getTopics();

    TopicDto getTopicById(Long id);

    TopicDto updateTopic(TopicInitDto topicInitDto, Long id);

    void deleteTopicById(Long id);
}
