package dev.alexcoss.universitycms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "students")
@ToString(exclude = "students")
@Entity
@Table(name = "course", schema = "university")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    @Column(name = "course_name")
    @NotEmpty(message = "{course.validation.not_empty}")
    @Size(min = 2, max = 100, message = "{course.validation.size}")
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    public void addStudentToCourse(Student student) {
        students.add(student);
        student.getCourses().add(this);
    }

    public void removeStudentFromCourse(Student student) {
        students.remove(student);
        student.getCourses().remove(this);
    }
}
