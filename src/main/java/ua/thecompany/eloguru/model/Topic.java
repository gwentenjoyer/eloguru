package ua.thecompany.eloguru.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="topic")
public class Topic extends BaseEntity{
    @Column(name="label")
    private String label;

//    @Column(name="contents")
    @OneToMany(fetch = FetchType.LAZY)
    private List<ContentObject> contents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinTable(name = "courses_topics",
//            joinColumns = @JoinColumn(name = "courses_id"),
//            inverseJoinColumns = @JoinColumn(name = "topic_id"))
    @JoinColumn(name = "course_id")
    private Course course;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinTable(name = "course_id")
//    private Course course;
}
