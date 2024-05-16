package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findAllByFirstNameStartingWith(String letter);

    @Query("SELECT t.username FROM Teacher t")
    Set<String> findAllUsernames();
}
