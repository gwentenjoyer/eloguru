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
@Table(name="topics")
public class Topic extends BaseEntity{
    @Column(name="label")
    private String label;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ContentObject> contents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

}
