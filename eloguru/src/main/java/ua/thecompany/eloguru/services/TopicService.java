package ua.thecompany.eloguru.services;

import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.StudentCourseProgressDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.model.Topic;

import java.util.List;

@Service
public interface TopicService {
    TopicDto createTopic(TopicInitDto topicInitDto);

    List<TopicDto> getTopics();

    List<TopicDto> getTopicsByCourse(Long courseId);

    TopicDto getTopicById(Long id);

    Topic getTopicModelById(Long id);

    TopicDto updateTopic(TopicInitDto topicInitDto, Long id);

    void deleteTopicById(Long id);

    void saveCompletedTopic(Long courseId, Long topicId, Long studentId);

    StudentCourseProgressDto getProgress(Long courseId, Long topicId, Long studentId);

    void removeCompletedTopic(Long courseId, Long topicId, Long studentId);
}
