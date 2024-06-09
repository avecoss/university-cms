package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import dev.alexcoss.universitycms.service.generator.TeacherBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeacherBuilder teacherBuilder;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    @PreAuthorize("hasRole('ADMIN')")
    public void testSaveTeacher() {
        TeacherCreateEditDTO teacherDto = TeacherCreateEditDTO.builder().user(getUserDTO()).build();
        Teacher teacher = Teacher.builder().user(new User()).build();

        when(teacherBuilder.buildEntity(any(TeacherCreateEditDTO.class))).thenReturn(teacher);
        when(userRepository.save(any())).thenReturn(null);

        teacherService.saveTeacher(teacherDto);

        verify(userRepository, times(1)).save(any());
        verify(teacherRepository, times(1)).save(any());
    }

    @Test
    @PreAuthorize("hasRole('ADMIN')")
    public void testUpdateTeacher() {
        Long teacherId = 1L;
        TeacherCreateEditDTO updated = TeacherCreateEditDTO.builder().user(getUserDTO()).build();
        User user = User.builder().firstName("editFirstname").lastName("editLastname").build();
        Teacher teacher = Teacher.builder().user(user).courses(Set.of()).build();

        Teacher teacherFromDB = Teacher.builder().user(User.builder().firstName("firstname").lastName("lastname").build())
            .courses(Set.of()).build();

        when(teacherBuilder.buildEntity(any(TeacherCreateEditDTO.class))).thenReturn(teacher);
        when(teacherRepository.findById(teacherId)).thenReturn(java.util.Optional.of(teacherFromDB));

        teacherService.updateTeacher(teacherId, updated);

        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    @PreAuthorize("hasRole('ADMIN')")
    public void testDeleteTeacherById() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(java.util.Optional.of(new Teacher()));

        teacherService.deleteTeacherById(teacherId);

        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(1)).deleteById(teacherId);
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
            .firstName("firstname")
            .lastName("lastname")
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }
}