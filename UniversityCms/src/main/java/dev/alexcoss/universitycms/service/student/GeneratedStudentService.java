package dev.alexcoss.universitycms.service.student;

import dev.alexcoss.universitycms.model.Student;

import java.util.List;

public interface GeneratedStudentService<T> extends StudentService {

    void saveStudents(List<T> list);

    List<Student> findAllStudents();
}
