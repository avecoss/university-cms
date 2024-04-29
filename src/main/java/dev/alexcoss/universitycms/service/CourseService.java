package dev.alexcoss.universitycms.service;

import java.util.List;
import java.util.Optional;

public interface CourseService<T> {

    List<T> findAllCourses();
    List<T> findCoursesByLetters(String letter);
    Optional<T> findCourseById(Integer id);
    void saveCourses(List<T> list);
    void updateCourse(Integer id, T updated);
    void saveCourse(T course);
    void deleteCourseById(Integer id);
}
