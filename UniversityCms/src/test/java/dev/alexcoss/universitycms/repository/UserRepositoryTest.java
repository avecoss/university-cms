package dev.alexcoss.universitycms.repository;

import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test-containers")
@Sql("/db/create-tables.sql")
class UserRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllUsernames() {
        User user1 = getUser("John", "Doe", "userJohn", "strongPass", "email@email.com",
            Authority.builder().role(Role.ADMIN).build());
        User user2 = getUser("Jane", "Bell", "user123", "password","user123@email.com",
            Authority.builder().role(Role.ADMIN).build(), Authority.builder().role(Role.STUDENT).build());

        userRepository.save(user1);
        userRepository.save(user2);

        Set<String> usernames = userRepository.findAllUsernames();

        assertEquals(2, usernames.size());
        assertTrue(usernames.contains("userJohn"));
        assertTrue(usernames.contains("user123"));
    }

    private @NotNull User getUser(String firstName, String lastName, String username, String password, String email, Authority... authorities) {
        return User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .email(email)
            .authorities(Set.of(authorities))
            .build();
    }
}