package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.users.TeacherDTO;

import java.util.List;
import java.util.Locale;

public interface TeacherService<T extends TeacherDTO, E extends TeacherDTO>{

    List<T> findAllTeachers();
    List<T> findTeachersByFirstName(String firstName);
    T findTeacherById(Long id, Locale locale);
    T findTeacherById(Long id);
    void saveTeachers(List<E> list);
    void updateTeacher(Long id, E updated, Locale locale);
    void updateTeacher(Long id, E updated);
    void saveTeacher(E Teacher, Locale locale);
    void saveTeacher(E Teacher);
    void deleteTeacherById(Long id);
}
