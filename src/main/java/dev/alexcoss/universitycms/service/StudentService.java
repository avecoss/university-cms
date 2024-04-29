package dev.alexcoss.universitycms.service;

import java.util.List;
import java.util.Optional;

public interface StudentService<T> {
    
    List<T> findAllStudents();
    List<T> findStudentsByFirstName(String firstName);
    List<T> findStudentsByCourse(String courseName);
    Optional<T> findStudentById(Long id);
    void saveStudents(List<T> list);
    void updateStudent(Long id, T updated);
    void saveStudent(T student);
    void deleteStudentById(Long id);
}
