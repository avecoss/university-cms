package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
        SELECT s FROM Course c
        JOIN c.students s
        WHERE c.name = :courseName
        """)
    List<Student> findByCoursesName(@Param("courseName") String courseName);

    @Query("""
        SELECT s FROM Student s
        WHERE UPPER(s.user.firstName) LIKE :letter
        """)
    List<Student> findAllByFirstNameStartingWith(@Param("letter") String letter);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
