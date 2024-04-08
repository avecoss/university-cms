package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
