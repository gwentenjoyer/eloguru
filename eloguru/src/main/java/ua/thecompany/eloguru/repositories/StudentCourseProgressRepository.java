package ua.thecompany.eloguru.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.thecompany.eloguru.model.StudentCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface StudentCourseProgressRepository extends JpaRepository<StudentCourseProgress, Long> {
	Optional<StudentCourseProgress> findByStudentIdAndCourseId(Long studentId, Long courseId);

	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
			"FROM StudentCourseProgress p " +
			"JOIN p.completedTopics t " +
			"WHERE p.id = :progressId AND t.id = :topicId")
	boolean isTopicCompleted(@Param("progressId") Long progressId, @Param("topicId") Long topicId);

//	@Modifying
//	@Transactional
//	@Query("UPDATE StudentCourseProgress p " +
//			"SET p.completedTopics = :updatedTopics " +
//			"WHERE p.id = :progressId")
//	void deleteCompletedTopic(@Param("progressId") Long progressId, @Param("updatedTopics") Topic updatedTopics);

//	@Query("DELETE FROM StudentCourseProgress p " +
//			"WHERE p.id = :progressId AND :topicId MEMBER OF p.completedTopics")
//	void deleteCompletedTopic(@Param("progressId") Long progressId, @Param("topicId") Topic topicId);

}
