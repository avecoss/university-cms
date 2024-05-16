package dev.alexcoss.universitygenerator.repository;

import dev.alexcoss.universitygenerator.model.Teacher;
import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<Teacher, Long> {
}
