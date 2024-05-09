package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.users.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.users.StudentViewDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
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
    @MockBean
    private PersonBuilder personBuilder;
    @MockBean
    private LoginPasswordGenerator loginPasswordGenerator;

    @Autowired
    private StudentServiceImpl studentService;

    @Test
    public void shouldSaveStudents() {
        List<StudentEditCreateDTO> studentList = getSampleStudentDtoList();
        studentService.saveStudents(studentList);

        verify(studentRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotSaveStudentsWhenListIsNull() {
        assertThrows(NullEntityListException.class, () -> studentService.saveStudents(null));

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotSaveStudentsWhenListIsEmpty() {
        assertThrows(NullEntityListException.class, () -> studentService.saveStudents(Collections.emptyList()));

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldNotSaveStudentsWhenListContainsInvalidStudent() {
        List<StudentEditCreateDTO> studentListWithInvalid = Arrays.asList(new StudentEditCreateDTO(), null);

        assertThrows(IllegalEntityException.class, () -> studentService.saveStudents(studentListWithInvalid));

        verify(studentRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    public void shouldSaveValidStudent() {
        StudentEditCreateDTO validStudent = StudentEditCreateDTO.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        Student student = new Student();
        student.setFirstName(validStudent.getFirstName());
        student.setLastName(validStudent.getLastName());

        when(personBuilder.buildEntity(validStudent)).thenReturn(student);
        when(loginPasswordGenerator.generateStartingLogin(student.getFirstName(), student.getLastName(),
            Collections.<String>emptySet())).thenReturn("studentLogin");
        when(loginPasswordGenerator.generateStartingPassword()).thenReturn("password");

        studentService.saveStudent(validStudent);

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void shouldNotSaveInvalidStudent() {
        StudentEditCreateDTO invalidStudent = new StudentEditCreateDTO();

        assertThrows(IllegalEntityException.class, () -> studentService.saveStudent(invalidStudent));

        verify(studentRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteExistingStudentById() {
        long existingStudentId = 1L;
        when(studentRepository.findById(existingStudentId)).thenReturn(Optional.of(new Student()));

        studentService.deleteStudentById(existingStudentId);

        verify(studentRepository, times(1)).deleteById(existingStudentId);
    }

    @Test
    public void shouldNotDeleteNonExistingStudentById() {
        long nonExistingStudentId = 99L;
        when(studentRepository.findById(nonExistingStudentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> studentService.deleteStudentById(nonExistingStudentId));
        verify(studentRepository, never()).deleteById(nonExistingStudentId);
    }

    @Test
    public void shouldUpdateExistingStudent() {
        long studentId = 1L;
        StudentEditCreateDTO studentDto = StudentEditCreateDTO.builder()
            .firstName("UpdatedFirstName")
            .lastName("UpdatedLastName")
            .build();

        Student student = getStudent(studentId, "firstname", "lastname", new Group(), new HashSet<>());

        when(personBuilder.buildEntity(studentDto)).thenReturn(student);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());

        studentService.updateStudent(studentId, studentDto);

        StudentViewDTO updatedStudentDto = studentService.findStudentById(studentId);

        assertEquals(updatedStudentDto.getFirstName(), studentDto.getFirstName());
        assertEquals(updatedStudentDto.getLastName(), studentDto.getLastName());
    }

    private List<StudentEditCreateDTO> getSampleStudentDtoList() {
        return Arrays.asList(
            StudentEditCreateDTO.builder().id(1).firstName("John").lastName("Doe").build(),
            StudentEditCreateDTO.builder().id(2).firstName("Jane").lastName("Smith").build()
        );
    }

    private Student getStudent(long id, String firstname, String lastname, Group group, Set<Course> courses) {
        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstname);
        student.setLastName(lastname);
        student.setUsername("username");
        student.setPassword("password");
        student.setRole(Role.STUDENT);
        student.setGroup(group);
        student.setCourses(courses);
        return student;
    }
}