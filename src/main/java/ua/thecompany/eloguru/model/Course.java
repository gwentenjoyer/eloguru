package ua.thecompany.eloguru.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="courses")
public class Course extends BaseEntity{

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "teacher_id", nullable = false)
//    private Teacher teacherAccountId;


    @Column(name="header")
    private String header;

    @Column(name="description", length = 2048)
    private String description;

    @Column(name="rating")
    private String rating;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="duration_days")
    private Integer durationDays;

    @Column(name="categories")
    @Enumerated(EnumType.STRING)
    private EnumeratedCategories categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private List<Topic> topics = new ArrayList<>();
}
