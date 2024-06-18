package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.thecompany.eloguru.dto.StudentCourseProgressDto;
import ua.thecompany.eloguru.model.StudentCourseProgress;
import ua.thecompany.eloguru.model.Topic;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StudentCourseProgressMapper {

	@Mapping(source = "student.id", target = "studentId")
	@Mapping(source = "course.id", target = "courseId")
	@Mapping(source = "completedTopics", target = "completedTopicIds", qualifiedByName = "mapTopicsToIds")
	StudentCourseProgressDto toDTO(StudentCourseProgress studentCourseProgress);

	@Mapping(source = "studentId", target = "student.id")
	@Mapping(source = "courseId", target = "course.id")
	@Mapping(source = "completedTopicIds", target = "completedTopics", qualifiedByName = "mapIdsToTopics")
	StudentCourseProgress toEntity(StudentCourseProgressDto studentCourseProgressDTO);

	@Named("mapTopicsToIds")
	default Set<Long> mapTopicsToIds(Set<Topic> topics) {
		return topics.stream()
				.map(Topic::getId)
				.collect(Collectors.toSet());
	}

	@Named("mapIdsToTopics")
	default Set<Topic> mapIdsToTopics(Set<Long> ids) {
		return ids.stream()
				.map(id -> {
					Topic topic = new Topic();
					topic.setId(id);
					return topic;
				})
				.collect(Collectors.toSet());
	}
}
