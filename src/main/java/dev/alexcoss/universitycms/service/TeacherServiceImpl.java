package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.users.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.users.TeacherViewDTO;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
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
public class TeacherServiceImpl implements TeacherService<TeacherViewDTO, TeacherCreateEditDTO> {

    private final TeacherRepository repository;
    private final PersonBuilder personBuilder;
    private final LoginPasswordGenerator loginPasswordGenerator;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public TeacherViewDTO findTeacherById(Long id, Locale locale) {
        return getTeacherDTO(id, locale);
    }

    @Override
    public TeacherViewDTO findTeacherById(Long id) {
        return getTeacherDTO(id, LocaleContextHolder.getLocale());
    }

    @Override
    public List<TeacherViewDTO> findAllTeachers() {
        List<Teacher> teachers = repository.findAll();
        return teachers.stream()
            .map(student -> modelMapper.map(student, TeacherViewDTO.class))
            .toList();
    }

    @Override
    public List<TeacherViewDTO> findTeachersByFirstName(String firstName) {
        List<Teacher> byFirstNameStartingWith = repository.findAllByFirstNameStartingWith(firstName);

        return byFirstNameStartingWith.stream()
            .map(teacher -> modelMapper.map(teacher, TeacherViewDTO.class))
            .toList();
    }

    @Transactional
    @Override
    public void saveTeachers(List<TeacherCreateEditDTO> teacherList) {
        isValidTeachersList(teacherList);

        List<Teacher> teachers = teacherList.stream()
            .map(teacherDTO -> modelMapper.map(teacherDTO, Teacher.class))
            .toList();

        repository.saveAllAndFlush(teachers);
    }

    @Transactional
    @Override
    public void saveTeacher(TeacherCreateEditDTO teacher, Locale locale) {
        isValidTeacher(teacher, locale);
        repository.save(buildTeacherWithLoginAndPass(teacher));
    }

    @Transactional
    @Override
    public void saveTeacher(TeacherCreateEditDTO teacher) {
        Locale locale = LocaleContextHolder.getLocale();
        isValidTeacher(teacher, locale);
        repository.save(buildTeacherWithLoginAndPass(teacher));
    }

    @Transactional
    @Override
    public void updateTeacher(Long id, TeacherCreateEditDTO updated) {
        updateTeacherFromDto(id, updated, LocaleContextHolder.getLocale());
    }

    @Transactional
    @Override
    public void updateTeacher(Long id, TeacherCreateEditDTO updated, Locale locale) {
        updateTeacherFromDto(id, updated, locale);
    }

    @Transactional
    @Override
    public void deleteTeacherById(Long teacherId) {
        repository.findById(teacherId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{teacherId}, "Teacher with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(teacherId);
    }

    private Set<String> findAllUsernames() {
        return repository.findAllUsernames();
    }

    private void isValidTeacher(TeacherCreateEditDTO teacher, Locale locale) {
        if (teacher == null || teacher.getFirstName() == null || teacher.getFirstName().isEmpty() ||
            teacher.getLastName() == null || teacher.getLastName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("teacher.errors.invalid", new Object[0],
                "Invalid teacher data", locale));
        }
    }

    private void isValidTeachersList(List<TeacherCreateEditDTO> teacherList) {
        Locale locale = LocaleContextHolder.getLocale();
        if (teacherList == null || teacherList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("student.errors.empty_list", new Object[0],
                "Teacher list is null or empty", locale));
        }

        for (TeacherCreateEditDTO teacher : teacherList) {
            isValidTeacher(teacher, locale);
        }
    }

    private TeacherViewDTO getTeacherDTO(Long id, Locale locale) {
        return repository.findById(id)
            .map(teacher -> modelMapper.map(teacher, TeacherViewDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{id}, "Teacher with ID {0} not found!", locale)));
    }

    private Teacher buildTeacherWithLoginAndPass(TeacherCreateEditDTO teacherDTO) {
        Teacher teacher = personBuilder.buildEntity(teacherDTO);
        teacher.setUsername(loginPasswordGenerator.generateStartingLogin(teacher.getFirstName(), teacher.getLastName(), findAllUsernames()));
        teacher.setPassword(loginPasswordGenerator.generateStartingPassword());

        return teacher;
    }

    private void updateTeacherFromDto(Long id, TeacherCreateEditDTO updated, Locale locale) {
        isValidTeacher(updated, locale);

        Teacher teacherWithCourses = personBuilder.buildEntity(updated);

        repository.findById(id)
            .map(teacher -> {
                teacher.setFirstName(teacherWithCourses.getFirstName());
                teacher.setLastName(teacherWithCourses.getLastName());
                teacher.setCourses(teacherWithCourses.getCourses());

                return teacher;
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{id}, "Teacher with ID {0} not found!", locale)));
    }
}
