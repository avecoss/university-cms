package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.StudentDTO;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {StudentService.class, ModelMapper.class})
class StudentServiceTest {
    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;


    @Test
    public void shouldAddStudents() {
        List<StudentDTO> studentList = getSampleStudentDtoList();
        studentService.addStudents(studentList);

        verify(studentRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotAddStudentsWhenListIsNull() {
        studentService.addStudents(null);

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotAddStudentsWhenListIsEmpty() {
        studentService.addStudents(Collections.emptyList());

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotAddStudentsWhenListContainsInvalidStudent() {
        List<StudentDTO> studentListWithInvalid = Arrays.asList(new StudentDTO(), null);
        studentService.addStudents(studentListWithInvalid);

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldAddValidStudent() {
        StudentDTO validStudent = StudentDTO.builder()
            .id(1)
            .firstName("John")
            .lastName("Doe")
            .username("user")
            .password("pass")
            .build();
        studentService.addStudent(validStudent);

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void shouldNotAddInvalidStudent() {
        StudentDTO invalidStudent = new StudentDTO();
        studentService.addStudent(invalidStudent);

        verify(studentRepository, never()).save(any());
    }

    @Test
    public void shouldRemoveExistingStudentById() {
        long existingStudentId = 1;
        when(studentRepository.findById(existingStudentId)).thenReturn(Optional.of(new Student()));

        studentService.removeStudentById(existingStudentId);

        verify(studentRepository, times(1)).deleteById(existingStudentId);
    }

    @Test
    public void shouldNotRemoveNonExistingStudentById() {
        long nonExistingStudentId = 99;
        when(studentRepository.findById(nonExistingStudentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> studentService.removeStudentById(nonExistingStudentId));
        verify(studentRepository, never()).deleteById(nonExistingStudentId);
    }

    private List<StudentDTO> getSampleStudentDtoList() {
        return Arrays.asList(
            StudentDTO.builder().id(1).firstName("John").lastName("Doe").username("user").password("pass").build(),
            StudentDTO.builder().id(2).firstName("Jane").lastName("Smith").username("user").password("pass").build()
        );
    }
}