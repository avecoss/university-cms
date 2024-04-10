package dev.alexcoss.universitycms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "courses", callSuper = true)
@ToString(exclude = "courses", callSuper = true)
@Entity
@Table(name = "students", schema = "university")
public class Student extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses;
}
