package ua.thecompany.eloguru.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="content")
public class ContentObject extends BaseEntity{

    @Enumerated
    @Column(name="type")
    private EnumeratedContentType type;

    @Column(name="label")
    private String label;

    @Column(name="essence")
    private String essence;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic", nullable = false)
    private Topic topic;
}
