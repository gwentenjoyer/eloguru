package ua.thecompany.eloguru.dto;

import ua.thecompany.eloguru.model.EnumeratedCategories;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public record CourseDto(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean active, Long teacherId,
                        String header, String description, String rating, Date startDate, Integer durationDays,
                        EnumeratedCategories categories, List<Long> topics, ArrayList<FeedbackDto> feedbacks, String photo)
        implements Serializable {}
