package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.StudentDTO;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.exception.IllegalEntityException;
import dev.alexcoss.universitycms.service.exception.NullEntityListException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {StudentServiceImpl.class, ModelMapper.class})
class StudentServiceImplTest {
    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentServiceImpl studentService;


    @Test
    public void shouldAddStudents() {
        List<StudentDTO> studentList = getSampleStudentDtoList();
        studentService.saveStudents(studentList);

        verify(studentRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotAddStudentsWhenListIsNull() {
        assertThrows(NullEntityListException.class, () -> studentService.saveStudents(null));

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotAddStudentsWhenListIsEmpty() {
        assertThrows(NullEntityListException.class, () -> studentService.saveStudents(Collections.emptyList()));

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotAddStudentsWhenListContainsInvalidStudent() {
        List<StudentDTO> studentListWithInvalid = Arrays.asList(new StudentDTO(), null);

        assertThrows(IllegalEntityException.class, () -> studentService.saveStudents(studentListWithInvalid));

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
        studentService.saveStudent(validStudent);

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void shouldNotAddInvalidStudent() {
        StudentDTO invalidStudent = new StudentDTO();

        assertThrows(IllegalEntityException.class, () -> studentService.saveStudent(invalidStudent));

        verify(studentRepository, never()).save(any());
    }

    @Test
    public void shouldRemoveExistingStudentById() {
        long existingStudentId = 1;
        when(studentRepository.findById(existingStudentId)).thenReturn(Optional.of(new Student()));

        studentService.deleteStudentById(existingStudentId);

        verify(studentRepository, times(1)).deleteById(existingStudentId);
    }

    @Test
    public void shouldNotRemoveNonExistingStudentById() {
        long nonExistingStudentId = 99;
        when(studentRepository.findById(nonExistingStudentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> studentService.deleteStudentById(nonExistingStudentId));
        verify(studentRepository, never()).deleteById(nonExistingStudentId);
    }

    private List<StudentDTO> getSampleStudentDtoList() {
        return Arrays.asList(
            StudentDTO.builder().id(1).firstName("John").lastName("Doe").username("user").password("pass").build(),
            StudentDTO.builder().id(2).firstName("Jane").lastName("Smith").username("user").password("pass").build()
        );
    }
}