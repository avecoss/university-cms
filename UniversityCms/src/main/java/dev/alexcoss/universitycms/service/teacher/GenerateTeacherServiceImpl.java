package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.dto.data.response.GTeacher;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerateTeacherServiceImpl implements GeneratedTeacherService<GTeacher> {

    private final TeacherRepository repository;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public void saveTeachers(List<GTeacher> gTeachers) {
        isValidTeachersList(gTeachers);

        List<Teacher> teachers = gTeachers.stream()
            .map(gTeacher -> Teacher.builder()
                .firstName(gTeacher.getFirstName())
                .lastName(gTeacher.getLastName())
                .username(gTeacher.getUsername())
                .password(gTeacher.getPassword())
                .role(gTeacher.getRole())
                .build())
            .collect(Collectors.toList());

        repository.saveAllAndFlush(teachers);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Teacher> findAllTeachers() {
        return repository.findAll();
    }

    private void isValidTeacher(GTeacher teacher, Locale locale) {
        if (teacher == null || teacher.getFirstName() == null || teacher.getFirstName().isEmpty() ||
            teacher.getLastName() == null || teacher.getLastName().isEmpty() ||
            teacher.getUsername() == null || teacher.getUsername().isEmpty() ||
            teacher.getPassword() == null || teacher.getPassword().isEmpty() ||
            teacher.getRole() == null) {

            throw new IllegalEntityException(messageSource.getMessage("teacher.errors.invalid", new Object[0],
                "Invalid teacher data", locale));
        }
    }

    private void isValidTeachersList(List<GTeacher> teacherList) {
        Locale locale = LocaleContextHolder.getLocale();
        if (teacherList == null || teacherList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("teacher.errors.empty_list", new Object[0],
                "Teacher list is null or empty", locale));
        }

        for (GTeacher teacher : teacherList) {
            isValidTeacher(teacher, locale);
        }
    }
}
