package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Student;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test-containers")
@Sql("/db/create-tables.sql")
public class StudentRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void testFindByCoursesName() {
        String courseName = "Math";
        Course course = new Course();
        course.setName(courseName);

        courseRepository.save(course);

        Student student1 = getStudent("John", "Doe", "userJohn", "strongPass");
        Student student2 = getStudent("Jane", "Bell", "user123", "password");

        course.addStudentToCourse(student1);
        course.addStudentToCourse(student2);

        studentRepository.save(student1);
        studentRepository.save(student2);

        List<Student> students = studentRepository.findByCoursesName(courseName);

        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Jane", students.get(1).getFirstName());
    }

    private @NotNull Student getStudent(String firstName, String lastName, String username, String password) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setUsername(username);
        student.setPassword(password);
        return student;
    }
}