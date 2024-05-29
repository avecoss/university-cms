package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.dto.view.users.TeacherDTO;

import java.util.List;
import java.util.Locale;

public interface TeacherProcessingService<T extends TeacherDTO, E extends TeacherDTO> extends TeacherService {

    List<T> findAllTeachers();

    List<T> findTeachersByFirstName(String firstName);

    T findTeacherById(Long id, Locale locale);

    T findTeacherById(Long id);

    void updateTeacher(Long id, E updated, Locale locale);

    void updateTeacher(Long id, E updated);

    void saveTeacher(E Teacher, Locale locale);

    void saveTeacher(E Teacher);

    void deleteTeacherById(Long id);
}
