package dev.alexcoss.universitycms.service.user;

import dev.alexcoss.universitycms.dto.view.users.PersonEditDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Person;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkPassword(String password, PersonEditDTO user) {
        return user.getRole() == Role.STUDENT ? checkPasswordForUser(password, studentRepository.findByUsername(user.getUsername())) :
                user.getRole() == Role.TEACHER ? checkPasswordForUser(password, teacherRepository.findByUsername(user.getUsername())) :
                user.getRole() == Role.ADMIN && checkPasswordForAdmin(password, user.getUsername());
    }

    @Transactional
    public void updateUser(PersonEditDTO user) {
        if (user.getRole() == Role.STUDENT) {
            updateStudent(user);
        } else if (user.getRole() == Role.TEACHER) {
            updateTeacher(user);
        } else if (user.getRole() == Role.ADMIN) {
            updateAdmin(user);
        }
    }

    private boolean checkPasswordForUser(String password, Optional<? extends Person> user) {
       return user.map(value -> passwordEncoder.matches(password, value.getPassword())).orElse(false);
    }

    private boolean checkPasswordForAdmin(String password, String username) {
        Optional<Student> optionalStudent = studentRepository.findByUsername(username);
        return optionalStudent.map(student -> passwordEncoder.matches(password, student.getPassword()))
            .orElseGet(() -> {
                Optional<Teacher> optionalTeacher = teacherRepository.findByUsername(username);
                return optionalTeacher.map(teacher -> passwordEncoder.matches(password, teacher.getPassword())).orElse(false);
            });
    }

    private void updateStudent(PersonEditDTO user) {
        studentRepository.findByUsername(user.getUsername()).ifPresent(student -> {
            updateUserInfo(student, user);
            studentRepository.save(student);
        });
    }

    private void updateTeacher(PersonEditDTO user) {
        teacherRepository.findByUsername(user.getUsername()).ifPresent(teacher -> {
            updateUserInfo(teacher, user);
            teacherRepository.save(teacher);
        });
    }

    private void updateAdmin(PersonEditDTO user) {
        Optional<Student> optionalStudent = studentRepository.findByUsername(user.getUsername());
        if (optionalStudent.isPresent()) {
            updateUserInfo(optionalStudent.get(), user);
            studentRepository.save(optionalStudent.get());
        } else {
            teacherRepository.findByUsername(user.getUsername()).ifPresent(teacher -> {
                updateUserInfo(teacher, user);
                teacherRepository.save(teacher);
            });
        }
    }

    private void updateUserInfo(Person user, PersonEditDTO userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
