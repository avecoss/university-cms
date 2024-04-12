package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@TestPropertySource(locations="classpath:application-test.yml")
public class StudentRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testFindByCoursesName() {
        Course course = new Course();
        course.setName("Math");

        Student student1 = new Student();
        student1.setFirstName("John");
        student1.getCourses().add(course);

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.getCourses().add(course);

        course.getStudents().add(student1);
        course.getStudents().add(student2);

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<Student> students = studentRepository.findByCoursesName("Math");

        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Jane", students.get(1).getFirstName());
    }
}