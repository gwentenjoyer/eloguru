package ua.thecompany.eloguru.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentCourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToMany
    @JoinTable(
            name = "progress_completed_topics",
            joinColumns = @JoinColumn(name = "progress_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> completedTopics;

    private double progressPercentage;
    private boolean isCompleted;

    public void updateProgress() {
        int totalTopics = course.getTopics().size();
        int completedTopicsCount = completedTopics.size();
        this.progressPercentage = (double) completedTopicsCount / totalTopics * 100;
        if (this.progressPercentage >= 100) {
            this.isCompleted = true;
        }
    }
}
