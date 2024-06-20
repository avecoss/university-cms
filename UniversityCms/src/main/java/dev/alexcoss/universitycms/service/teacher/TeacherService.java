package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.dto.view.teacher.TeacherDTO;

import java.util.List;
import java.util.Locale;

public interface TeacherService<T extends TeacherDTO, E extends TeacherDTO> {

    List<T> getAllTeachers();

    List<T> getTeachersByFirstName(String firstName);

    T getTeacherById(Long id, Locale locale);

    T getTeacherById(Long id);

    void updateTeacher(E updated, Locale locale);

    void updateTeacher(E updated);

    void saveTeacher(E Teacher, Locale locale);

    void saveTeacher(E Teacher);

    void deleteTeacherById(Long id);
}
