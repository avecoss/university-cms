package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.users.StudentDTO;

import java.util.List;
import java.util.Locale;

public interface StudentService<T extends StudentDTO, E extends StudentDTO> {
    
    List<T> findAllStudents();
    List<T> findStudentsByFirstName(String firstName);
    List<T> findStudentsByCourse(String courseName);
    T findStudentById(Long id);
    T findStudentById(Long id, Locale locale);
    void saveStudents(List<E> list);
    void updateStudent(Long id, E updated);
    void updateStudent(Long id, E updated, Locale locale);
    void saveStudent(E student);
    void saveStudent(E student, Locale locale);
    void deleteStudentById(Long id);
}
