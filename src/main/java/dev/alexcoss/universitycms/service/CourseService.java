package dev.alexcoss.universitycms.service;

import java.util.List;
import java.util.Locale;

public interface CourseService<T> {

    List<T> findAllCourses();
    List<T> findCoursesByLetters(String letter);
    T findCourseById(Integer id);
    T findCourseById(Integer id, Locale locale);
    void saveCourses(List<T> list);
    void updateCourse(Integer id, T updated);
    void saveCourse(T course);
    void deleteCourseById(Integer id);
}
