package dev.alexcoss.universitycms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "students")
@ToString(exclude = "students")
@Entity
@Table(name = "group", schema = "university")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Student> students;

    public void addStudent(Student student) {
        students.add(student);
        student.setGroup(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setGroup(null);
    }
}
