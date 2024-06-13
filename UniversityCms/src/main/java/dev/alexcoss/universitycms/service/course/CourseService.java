package dev.alexcoss.universitycms.service.course;

import java.util.List;
import java.util.Locale;

public interface CourseService<T>{

    List<T> getAllCourses();
    List<T> getCoursesByLetters(String letter);
    T getCourseById(Integer id);
    T getCourseById(Integer id, Locale locale);
    List<T> getAllByIds(Iterable<Integer> ids);
    void updateCourse(T updated);
    void saveCourse(T course);
    void deleteCourseById(Integer id);
}
