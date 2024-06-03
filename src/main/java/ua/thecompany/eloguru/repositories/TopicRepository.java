package ua.thecompany.eloguru.repositories;

import ua.thecompany.eloguru.model.Topic;

import java.util.List;

public interface TopicRepository extends BaseRepository<Topic, Long>{
    List<Topic> findByActiveAndCourseId(boolean active, Long courseId);
}
