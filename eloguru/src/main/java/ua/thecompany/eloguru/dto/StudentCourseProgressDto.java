package ua.thecompany.eloguru.dto;

import java.io.Serializable;
import java.util.Set;

public record StudentCourseProgressDto(Long id, Long studentId, Long courseId,
		Set<Long> completedTopicIds, double progressPercentage, boolean isCompleted) implements Serializable {
}
