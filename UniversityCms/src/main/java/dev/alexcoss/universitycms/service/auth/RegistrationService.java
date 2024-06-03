package dev.alexcoss.universitycms.service.auth;

import dev.alexcoss.universitycms.dto.view.users.PersonAuthDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(PersonAuthDTO person) {
        Student student = modelMapper.map(person, Student.class);
        student.setRole(Role.STUDENT);
        student.setPassword(passwordEncoder.encode(person.getPassword()));

        studentRepository.save(student);
    }
}
