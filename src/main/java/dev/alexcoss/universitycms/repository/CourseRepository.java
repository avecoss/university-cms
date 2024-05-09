package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findAllByNameStartingWith(String letter);
}