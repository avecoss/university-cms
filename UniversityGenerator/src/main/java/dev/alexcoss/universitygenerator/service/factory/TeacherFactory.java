package dev.alexcoss.universitygenerator.service.factory;

import dev.alexcoss.universitygenerator.dto.TeacherDTO;
import dev.alexcoss.universitygenerator.enumerated.Role;
import org.springframework.stereotype.Component;

@Component
public class TeacherFactory implements PersonFactory<TeacherDTO> {
    @Override
    public TeacherDTO createPerson(String firstName, String lastName, String username, String password) {
        return TeacherDTO.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .role(Role.TEACHER)
            .build();
    }
}
