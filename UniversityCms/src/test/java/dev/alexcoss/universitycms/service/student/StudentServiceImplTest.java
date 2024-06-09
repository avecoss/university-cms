package dev.alexcoss.universitycms.service.student;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import dev.alexcoss.universitycms.service.generator.StudentBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentBuilder studentBuilder;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    @PreAuthorize("hasRole('ADMIN')")
    public void testSaveStudent() {
        StudentEditCreateDTO studentDto = StudentEditCreateDTO.builder().user(getUserDTO()).build();
        Student student = Student.builder().user(new User()).build();

        when(studentBuilder.buildEntity(any(StudentEditCreateDTO.class))).thenReturn(student);
        when(userRepository.save(any())).thenReturn(null);

        studentService.saveStudent(studentDto);

        verify(userRepository, times(1)).save(any());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    @PreAuthorize("hasRole('ADMIN')")
    public void testUpdateStudent() {
        Long studentId = 1L;
        StudentEditCreateDTO updated = StudentEditCreateDTO.builder().user(getUserDTO()).build();
        User user = User.builder().firstName("editFirstname").lastName("editLastname").build();
        Student student = Student.builder().user(user).group(new Group()).courses(Set.of()).build();

        Student studentFromDB = Student.builder().user(User.builder().firstName("firstname").lastName("lastname").build())
            .group(new Group()).courses(Set.of()).build();

        when(studentBuilder.buildEntity(any(StudentEditCreateDTO.class))).thenReturn(student);
        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(studentFromDB));

        studentService.updateStudent(studentId, updated);

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    @PreAuthorize("hasRole('ADMIN')")
    public void testDeleteStudentById() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(new Student()));

        studentService.deleteStudentById(studentId);

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
            .firstName("firstname")
            .lastName("lastname")
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }
}