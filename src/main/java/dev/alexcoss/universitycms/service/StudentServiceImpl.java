package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.users.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.users.StudentViewDTO;
import dev.alexcoss.universitycms.exception.EntityNotExistException;
import dev.alexcoss.universitycms.exception.IllegalEntityException;
import dev.alexcoss.universitycms.exception.NullEntityListException;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService<StudentViewDTO, StudentEditCreateDTO> {

    private final StudentRepository repository;
    private final PersonBuilder personBuilder;
    private final LoginPasswordGenerator loginPasswordGenerator;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public StudentViewDTO findStudentById(Long id) {
        return getStudentDTO(id, LocaleContextHolder.getLocale());
    }

    @Override
    public StudentViewDTO findStudentById(Long id, Locale locale) {
        return getStudentDTO(id, locale);
    }

    @Override
    public List<StudentViewDTO> findStudentsByCourse(String courseName) {
        List<Student> students = repository.findByCoursesName(courseName);
        return students.stream()
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .toList();
    }

    @Override
    public List<StudentViewDTO> findAllStudents() {
        List<Student> students = repository.findAll();
        return students.stream()
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .toList();
    }

    @Override
    public List<StudentViewDTO> findStudentsByFirstName(String name) {
        List<Student> byFirstNameStartingWith = repository.findAllByFirstNameStartingWith(name);

        return byFirstNameStartingWith.stream()
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .toList();
    }

    @Transactional
    @Override
    public void saveStudents(List<StudentEditCreateDTO> studentList) {
        isValidStudentList(studentList);

        List<Student> students = studentList.stream()
            .map(studentDTO -> modelMapper.map(studentDTO, Student.class))
            .toList();

        repository.saveAllAndFlush(students);
    }

    @Transactional
    @Override
    public void saveStudent(StudentEditCreateDTO student) {
        Locale locale = LocaleContextHolder.getLocale();
        isValidStudent(student, locale);
        repository.save(buildStudentWithLoginAndPass(student));
    }

    @Transactional
    @Override
    public void saveStudent(StudentEditCreateDTO student, Locale locale) {
        isValidStudent(student, locale);
        repository.save(buildStudentWithLoginAndPass(student));
    }

    @Transactional
    @Override
    public void updateStudent(Long id, StudentEditCreateDTO updated) {
        updateStudentFromDto(id, updated, LocaleContextHolder.getLocale());
    }

    @Override
    public void updateStudent(Long id, StudentEditCreateDTO updated, Locale locale) {
        updateStudentFromDto(id, updated, locale);
    }

    @Transactional
    @Override
    public void deleteStudentById(Long studentId) {
        repository.findById(studentId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{studentId}, "Student with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(studentId);
    }

    private Set<String> findAllUsernames() {
        return repository.findAllUsernames();
    }

    private void isValidStudent(StudentEditCreateDTO student, Locale locale) {
        if (student == null || student.getFirstName() == null || student.getFirstName().isEmpty() ||
            student.getLastName() == null || student.getLastName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("student.errors.invalid", new Object[0],
                "Invalid student data", locale));
        }
    }

    private void isValidStudentList(List<StudentEditCreateDTO> studentList) {
        Locale locale = LocaleContextHolder.getLocale();
        if (studentList == null || studentList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("student.errors.empty_list", new Object[0],
                "Student list is null or empty", locale));
        }

        for (StudentEditCreateDTO student : studentList) {
            isValidStudent(student, locale);
        }
    }

    private StudentViewDTO getStudentDTO(Long id, Locale locale) {
        return repository.findById(id)
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{id}, "Student with ID {0} not found!", locale)));
    }

    private Student buildStudentWithLoginAndPass(StudentEditCreateDTO studentDTO) {
        Student student = personBuilder.buildEntity(studentDTO);
        student.setUsername(loginPasswordGenerator.generateStartingLogin(student.getFirstName(), student.getLastName(), findAllUsernames()));
        student.setPassword(loginPasswordGenerator.generateStartingPassword());

        return student;
    }

    private void updateStudentFromDto(Long id, StudentEditCreateDTO updated, Locale locale) {
        isValidStudent(updated, locale);

        Student buildStudent = personBuilder.buildEntity(updated);

        repository.findById(id)
            .map(student -> {
                student.setFirstName(buildStudent.getFirstName());
                student.setLastName(buildStudent.getLastName());
                student.setCourses(buildStudent.getCourses());
                student.setGroup(buildStudent.getGroup());

                return student;
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{id}, "Student with ID {0} not found!", locale)));
    }
}
