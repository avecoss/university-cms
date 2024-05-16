package dev.alexcoss.universitygenerator.service.factory;

import dev.alexcoss.universitygenerator.dto.StudentDTO;
import dev.alexcoss.universitygenerator.enumerated.Role;
import org.springframework.stereotype.Component;

@Component
public class StudentFactory implements PersonFactory<StudentDTO> {
    @Override
    public StudentDTO createPerson(String firstName, String lastName, String username, String password) {
        return StudentDTO.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(password)
            .role(Role.STUDENT)
            .build();
    }
}
