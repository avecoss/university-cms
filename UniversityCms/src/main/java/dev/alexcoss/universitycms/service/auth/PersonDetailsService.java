package dev.alexcoss.universitycms.service.auth;

import dev.alexcoss.universitycms.dto.view.users.PersonAuthDTO;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByUsername(username);
        if (student.isPresent()) {
            PersonAuthDTO foundStudent = modelMapper.map(student.get(), PersonAuthDTO.class);
            return new PersonDetails(foundStudent);
        }

        Optional<Teacher> teacher = teacherRepository.findByUsername(username);
        if (teacher.isPresent()) {
            PersonAuthDTO foundTeacher = modelMapper.map(teacher.get(), PersonAuthDTO.class);
            return new PersonDetails(foundTeacher);
        }

        throw new UsernameNotFoundException("User not found");
    }
}
