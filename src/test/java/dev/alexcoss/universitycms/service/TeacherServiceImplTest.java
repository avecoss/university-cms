package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.users.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.users.TeacherDTO;
import dev.alexcoss.universitycms.exception.EntityNotExistException;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TeacherServiceImpl.class, ModelMapper.class})
class TeacherServiceImplTest {

    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private PersonBuilder personBuilder;
    @MockBean
    private LoginPasswordGenerator loginPasswordGenerator;

    @Autowired
    private TeacherServiceImpl teacherService;

    @Test
    void testFindTeacherById() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        TeacherDTO teacherDTO = teacherService.findTeacherById(id);

        assertNotNull(teacherDTO);
        assertEquals(id, teacherDTO.getId());
    }

    @Test
    void testNotFoundTeacherById() {
        Long id = 9L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> teacherService.findTeacherById(id));
    }

    @Test
    void testSaveTeacher() {
        TeacherCreateEditDTO validTeacher = TeacherCreateEditDTO.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        Teacher teacher = new Teacher();
        teacher.setFirstName(validTeacher.getFirstName());
        teacher.setLastName(validTeacher.getLastName());

        when(personBuilder.buildEntity(validTeacher)).thenReturn(teacher);
        when(loginPasswordGenerator.generateStartingLogin(teacher.getFirstName(), teacher.getLastName(),
            Collections.<String>emptySet())).thenReturn("studentLogin");
        when(loginPasswordGenerator.generateStartingPassword()).thenReturn("password");

        teacherService.saveTeacher(validTeacher);

        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }
}