package dev.alexcoss.universitygenerator.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "courses", callSuper = true)
@ToString(exclude = "courses", callSuper = true)
@Entity
@Table(name = "student", schema = "university")
public class Student extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany
    @JoinTable(
        name = "student_course", schema = "university",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }

    public void setGroup(Group group) {
        this.group = group;
        group.getStudents().add(this);
    }

    public void removeGroup(Group group) {
        this.group = null;
        group.getStudents().remove(this);
    }
}
