package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
