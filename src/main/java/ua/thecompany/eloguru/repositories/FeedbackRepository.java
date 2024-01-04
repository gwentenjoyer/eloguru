package ua.thecompany.eloguru.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.thecompany.eloguru.model.Feedback;

import java.util.List;

@Repository
public interface FeedbackRepository extends BaseRepository<Feedback, Long>{

    @Query("select t from #{#entityName} t where t.course.id = :courseId and t.active = true")
    List<Feedback> findByCourse(@Param("courseId") Long courseId);

}
