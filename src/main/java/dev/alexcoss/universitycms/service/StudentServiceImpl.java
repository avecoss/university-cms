package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.StudentDTO;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.exception.IllegalEntityException;
import dev.alexcoss.universitycms.service.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService<StudentDTO> {

    private final StudentRepository repository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public Optional<StudentDTO> findStudentById(Long id) {
        return repository.findById(id)
            .map(student -> modelMapper.map(student, StudentDTO.class));

    }

    @Override
    public List<StudentDTO> findStudentsByCourse(String courseName) {
        List<Student> students = repository.findByCoursesName(courseName);
        return students.stream()
            .map(student -> modelMapper.map(student, StudentDTO.class))
            .toList();
    }

    @Override
    public List<StudentDTO> findAllStudents() {
        List<Student> students = repository.findAll();
        return students.stream()
            .map(student -> modelMapper.map(student, StudentDTO.class))
            .toList();
    }

    @Override
    public List<StudentDTO> findStudentsByFirstName(String name) {
        List<Student> byFirstNameStartingWith = repository.findAllByFirstNameStartingWith(name);

        return byFirstNameStartingWith.stream()
            .map(student -> modelMapper.map(student, StudentDTO.class))
            .toList();
    }

    @Transactional
    @Override
    public void saveStudents(List<StudentDTO> studentList) {
        isValidStudentList(studentList);

        List<Student> students = studentList.stream()
            .map(studentDTO -> modelMapper.map(studentDTO, Student.class))
            .toList();

        repository.saveAllAndFlush(students);
    }

    @Transactional
    @Override
    public void saveStudent(StudentDTO student) {
        isValidStudent(student);
        repository.save(modelMapper.map(student, Student.class));
    }

    @Transactional
    @Override
    public void updateStudent(Long id, StudentDTO updated) {
        isValidStudent(updated);

        repository.findById(id)
            .map(student -> {
                student.setFirstName(updated.getFirstName());
                student.setLastName(updated.getLastName());
                student.setUsername(updated.getUsername());
                student.setPassword(updated.getPassword());
                student.setGroup(modelMapper.map(updated.getGroup(), Group.class));

                return repository.save(student);
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[] {id}, "Student with ID {0} not found!", LocaleContextHolder.getLocale())));
    }

    @Transactional
    @Override
    public void deleteStudentById(Long studentId) {
        repository.findById(studentId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[] {studentId}, "Student with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(studentId);
    }

    private void isValidStudent(StudentDTO student) {
        if (student == null || student.getFirstName() == null || student.getFirstName().isEmpty() ||
            student.getLastName() == null || student.getLastName().isEmpty() ||
            student.getUsername() == null || student.getUsername().isEmpty() ||
            student.getPassword() == null || student.getPassword().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("student.errors.invalid", new Object[0],
                "Invalid student data", LocaleContextHolder.getLocale()));
        }
    }

    private void isValidStudentList(List<StudentDTO> studentList) {
        if (studentList == null || studentList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("student.errors.empty_list", new Object[0],
                "Student list is null or empty", LocaleContextHolder.getLocale()));
        }

        for (StudentDTO student : studentList) {
            isValidStudent(student);
        }
    }
}
