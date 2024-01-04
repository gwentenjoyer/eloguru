package ua.thecompany.eloguru.dto;

import ua.thecompany.eloguru.model.EnumeratedCategories;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public record CourseDto(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean active, Long teacherAccountId,
                        String header, String description, String rating, Date startDate, Integer durationDays,
                        EnumeratedCategories categories, ArrayList<TopicDto> topics, ArrayList<FeedbackDto> feedbacks)
        implements Serializable {}
