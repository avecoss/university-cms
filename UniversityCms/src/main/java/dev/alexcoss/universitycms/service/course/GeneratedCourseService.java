package dev.alexcoss.universitycms.service.course;

import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;

import java.util.List;
import java.util.Map;

public interface GeneratedCourseService<T> extends CourseService<T> {
    void saveCourses(List<T> list, Map<String, Teacher> teacherMap, Map<String, Student> studentMap);
}
