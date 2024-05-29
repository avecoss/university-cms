package dev.alexcoss.universitycms.service.course;

import java.util.List;
import java.util.Locale;

public interface CourseProcessingService<T> extends CourseService<T>{

    List<T> findAllCourses();
    List<T> findCoursesByLetters(String letter);
    T findCourseById(Integer id);
    T findCourseById(Integer id, Locale locale);
    List<T> findAllByIds(Iterable<Integer> ids);
    void updateCourse(Integer id, T updated);
    void saveCourse(T course);
    void deleteCourseById(Integer id);
}
