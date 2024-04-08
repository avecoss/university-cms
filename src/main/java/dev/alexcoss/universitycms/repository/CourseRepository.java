package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}