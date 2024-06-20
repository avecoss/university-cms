package dev.alexcoss.universitycms.service.student;

import dev.alexcoss.universitycms.dto.view.student.StudentDTO;

import java.util.List;
import java.util.Locale;

public interface StudentService<T extends StudentDTO, E extends StudentDTO> {

    List<T> getAllStudents();

    List<T> getStudentsByFirstName(String firstName);

    List<T> getStudentsByCourse(String courseName);

    T getStudentById(Long id);

    T getStudentById(Long id, Locale locale);

    void updateStudent(E updated);

    void updateStudent(E updated, Locale locale);

    void saveStudent(E student);

    void saveStudent(E student, Locale locale);

    void deleteStudentById(Long id);
}
