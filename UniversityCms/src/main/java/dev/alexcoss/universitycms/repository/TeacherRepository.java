package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("""
        SELECT t FROM Teacher t
        WHERE UPPER(t.user.firstName) LIKE :letter
        """)
    List<Teacher> findAllByFirstNameStartingWith(@Param("letter") String letter);
}
