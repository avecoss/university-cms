package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.model.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test-containers")
@Sql("/db/create-tables.sql")
class TeacherRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllByFirstNameStartingWith() {
        Teacher teacher1 = getTeacher("John", "Doe", "userJohn", "strongPass", "email@email.com", Authority.builder().role(Role.TEACHER).build());
        Teacher teacher2 = getTeacher("Jane", "Bell", "user123", "password", "user1@email.com", Authority.builder().role(Role.TEACHER).build());
        Teacher teacher3 = getTeacher("Jack", "Smith", "userJack", "pass1234", "user2@email.com", Authority.builder().role(Role.TEACHER).build());

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        List<Teacher> teachers = teacherRepository.findAllByFirstNameStartingWith("J%");

        assertEquals(3, teachers.size());
        assertTrue(teachers.stream().anyMatch(s -> s.getUser().getFirstName().equals("John")));
        assertTrue(teachers.stream().anyMatch(s -> s.getUser().getFirstName().equals("Jane")));
        assertTrue(teachers.stream().anyMatch(s -> s.getUser().getFirstName().equals("Jack")));
    }


    private @NotNull Teacher getTeacher(String firstName, String lastName, String username, String password, String email, Authority... authorities) {
        User user = User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .email(email)
            .authorities(Set.of(authorities))
            .build();

        user = userRepository.save(user);

        return Teacher.builder()
            .user(user)
            .build();
    }
}