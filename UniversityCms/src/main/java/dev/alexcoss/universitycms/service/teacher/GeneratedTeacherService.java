package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.model.Teacher;

import java.util.List;

public interface GeneratedTeacherService<T> extends TeacherService {
    void saveTeachers(List<T> list);

    List<Teacher> findAllTeachers();
}
