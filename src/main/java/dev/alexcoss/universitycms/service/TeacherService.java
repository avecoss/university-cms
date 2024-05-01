package dev.alexcoss.universitycms.service;

import java.util.List;
import java.util.Locale;

public interface TeacherService<T>{

    List<T> findAllTeachers();
    List<T> findTeachersByFirstName(String firstName);
    T findTeacherById(Long id, Locale locale);
    T findTeacherById(Long id);
    void saveTeachers(List<T> list);
    void updateTeacher(Long id, T updated, Locale locale);
    void updateTeacher(Long id, T updated);
    void saveTeacher(T Teacher, Locale locale);
    void saveTeacher(T Teacher);
    void deleteTeacherById(Long id);
}
