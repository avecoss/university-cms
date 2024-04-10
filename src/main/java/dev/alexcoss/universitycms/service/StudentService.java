package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.StudentDTO;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.exception.IllegalEntityException;
import dev.alexcoss.universitycms.service.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository repository;
    private final ModelMapper modelMapper;

    public Optional<StudentDTO> getStudentById(long id) {
        return repository.findById(id)
            .map(student -> modelMapper.map(student, StudentDTO.class));

    }

    public List<StudentDTO> getStudentsByCourse(String courseName) {
        List<Student> students = repository.findByCoursesName(courseName);
        return students.stream()
            .map(student -> modelMapper.map(student, StudentDTO.class))
            .toList();
    }

    public List<StudentDTO> getStudents() {
        List<Student> students = repository.findAll();
        return students.stream()
            .map(student -> modelMapper.map(student, StudentDTO.class))
            .toList();
    }

    @Transactional
    public void addStudents(List<StudentDTO> studentList) {
        isValidStudentList(studentList);

        List<Student> students = studentList.stream()
            .map(studentDTO -> modelMapper.map(studentDTO, Student.class))
            .toList();

        repository.saveAllAndFlush(students);
    }

    @Transactional
    public void addStudent(StudentDTO student) {
        isValidStudent(student);
        repository.save(modelMapper.map(student, Student.class));
    }

    @Transactional
    public void removeStudentById(long studentId) {
        Optional<StudentDTO> existingStudent = getStudentById(studentId);

        if (existingStudent.isPresent()) {
            repository.deleteById(studentId);
        } else {
            throw new EntityNotExistException("Student with ID " + studentId + " not found");
        }
    }

    private void isValidStudent(StudentDTO student) {
        if (student != null && student.getFirstName() != null && student.getLastName() != null &&
            student.getUsername() != null && student.getPassword() != null) {
            throw new IllegalEntityException("Invalid student data");
        }
    }

    private void isValidStudentList(List<StudentDTO> studentList) {
        if (studentList == null || studentList.isEmpty()) {
            throw new NullEntityListException("Student list is null or empty");
        }

        for (StudentDTO student : studentList) {
            isValidStudent(student);
        }
    }
}
