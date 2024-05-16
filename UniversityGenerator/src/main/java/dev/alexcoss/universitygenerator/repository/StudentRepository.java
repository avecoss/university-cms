package dev.alexcoss.universitygenerator.repository;

import dev.alexcoss.universitygenerator.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
