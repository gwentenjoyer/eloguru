package ua.thecompany.eloguru.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.thecompany.eloguru.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends BaseRepository<Course, Long> {
    Page<Course> findByHeaderContainingIgnoreCase(Pageable pageable, String headerPart);

    @Override
    @Query("select t from #{#entityName} t where t.active = true")
    Page<Course> findAll(Pageable pageable);


////    @Override
//    @Query("select t.topics.id from #{#entityName} t where t.id = :courseId")
//    List<Long> selectTopicsByCourseId(Long courseId);
    @Query("select t.id from  #{#entityName} c join c.topics t where c.id = :courseId")
    List<Long> selectTopicsByCourseId(@Param("courseId") Long courseId);

    Page<Course> findAllByOrderByRatingAsc(Pageable pageable);
    Page<Course> findAllByOrderByRatingDesc(Pageable pageable);
    Page<Course> findAllByOrderByDurationDaysAsc(Pageable pageable);
    Page<Course> findAllByOrderByDurationDaysDesc(Pageable pageable);

    @Query("select count(e) > 0 from Course c join c.students e where c.id = :courseId and e.id = :studentId")
    boolean isUserEnrolledInCourse(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
}
