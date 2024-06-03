package dev.alexcoss.universitycms.util;

import dev.alexcoss.universitycms.dto.view.users.PersonAuthDTO;
import dev.alexcoss.universitycms.model.Person;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.service.student.StudentServiceImpl;
import dev.alexcoss.universitycms.service.teacher.TeacherService;
import dev.alexcoss.universitycms.service.teacher.TeacherServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {

    private final TeacherServiceImpl teacherService;
    private final StudentServiceImpl studentService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonAuthDTO person = (PersonAuthDTO) target;
        boolean studentExists = studentService.findPersonByUsername(person.getUsername());
        boolean teacherExists = teacherService.findPersonByUsername(person.getUsername());

        if (studentExists || teacherExists) {
            errors.rejectValue("username", "Username already exists", "Username already exists");
        }
    }
}
