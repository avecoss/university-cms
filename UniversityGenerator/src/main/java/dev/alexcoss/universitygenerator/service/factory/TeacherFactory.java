package dev.alexcoss.universitygenerator.service.factory;

import dev.alexcoss.universitygenerator.dto.GTeacher;
import dev.alexcoss.universitygenerator.enumerated.Role;
import org.springframework.stereotype.Component;

@Component
public class TeacherFactory implements PersonFactory<GTeacher> {
    @Override
    public GTeacher createPerson(String firstName, String lastName, String username, String password) {
        return GTeacher.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .role(Role.TEACHER)
            .build();
    }
}
