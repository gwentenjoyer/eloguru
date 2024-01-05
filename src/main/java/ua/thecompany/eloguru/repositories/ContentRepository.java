package ua.thecompany.eloguru.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.thecompany.eloguru.model.ContentObject;
import ua.thecompany.eloguru.model.Feedback;

import java.util.List;

public interface ContentRepository extends BaseRepository<ContentObject, Long> {

    @Query("select t from #{#entityName} t where t.topic.id = :topicId and t.active = true")
    List<ContentObject> findByTopic(@Param("topicId") Long topicId);
}
