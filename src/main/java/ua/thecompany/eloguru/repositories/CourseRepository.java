package ua.thecompany.eloguru.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.thecompany.eloguru.model.Course;

@Repository
public interface CourseRepository extends BaseRepository<Course, Long> {
    Page<Course> findByHeaderContaining(Pageable pageable, String headerPart);

    @Override
    @Query("select t from #{#entityName} t where t.active = true")
    Page<Course> findAll(Pageable pageable);
}
