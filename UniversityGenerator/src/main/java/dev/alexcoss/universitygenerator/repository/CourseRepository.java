package dev.alexcoss.universitygenerator.repository;

import dev.alexcoss.universitygenerator.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {
}
