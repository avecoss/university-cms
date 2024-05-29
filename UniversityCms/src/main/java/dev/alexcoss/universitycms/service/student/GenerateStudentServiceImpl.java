package dev.alexcoss.universitycms.service.student;

import dev.alexcoss.universitycms.dto.data.response.GStudent;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerateStudentServiceImpl implements GeneratedStudentService<GStudent> {

    private final StudentRepository repository;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public void saveStudents(List<GStudent> gStudents) {
        isValidStudentList(gStudents);

        List<Student> students = gStudents.stream()
            .map(gStudent -> Student.builder()
                .firstName(gStudent.getFirstName())
                .lastName(gStudent.getLastName())
                .username(gStudent.getUsername())
                .password(gStudent.getPassword())
                .role(gStudent.getRole())
                .build())
            .collect(Collectors.toList());

        repository.saveAllAndFlush(students);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> findAllStudents() {
        return repository.findAll();
    }

    private void isValidStudent(GStudent student, Locale locale) {
        if (student == null || student.getFirstName() == null || student.getFirstName().isEmpty() ||
            student.getLastName() == null || student.getLastName().isEmpty() ||
            student.getUsername() == null || student.getUsername().isEmpty() ||
            student.getPassword() == null || student.getPassword().isEmpty() ||
            student.getRole() == null) {
            throw new IllegalEntityException(messageSource.getMessage("student.errors.invalid", new Object[0],
                "Invalid student data", locale));
        }
    }

    private void isValidStudentList(List<GStudent> studentList) {
        Locale locale = LocaleContextHolder.getLocale();
        if (studentList == null || studentList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("student.errors.empty_list", new Object[0],
                "Student list is null or empty", locale));
        }

        for (GStudent student : studentList) {
            isValidStudent(student, locale);
        }
    }
}
