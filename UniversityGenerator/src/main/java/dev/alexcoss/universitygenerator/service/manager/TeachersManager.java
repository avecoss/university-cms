package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.TeacherDTO;
import dev.alexcoss.universitygenerator.enumerated.Role;
import dev.alexcoss.universitygenerator.service.generator.PersonGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeachersManager {

    private final PersonGenerator<TeacherDTO> personGenerator;

    public List<TeacherDTO> getTeachers(int amount) {
        return personGenerator.generatePersons(Role.TEACHER, amount);
    }
}
