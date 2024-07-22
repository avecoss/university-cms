package dev.alexcoss.universitygenerator.service.factory;

import dev.alexcoss.universitygenerator.dto.GStudent;
import dev.alexcoss.universitygenerator.enumerated.Role;
import org.springframework.stereotype.Component;

@Component
public class StudentFactory implements PersonFactory<GStudent> {
    @Override
    public GStudent createPerson(String firstName, String lastName, String username, String password) {
        return GStudent.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .role(Role.STUDENT)
            .build();
    }
}
