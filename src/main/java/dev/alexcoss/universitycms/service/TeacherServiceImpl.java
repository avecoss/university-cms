package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.exception.IllegalEntityException;
import dev.alexcoss.universitycms.service.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService<TeacherDTO> {

    private final TeacherRepository repository;
    private final CourseService<CourseDTO> courseService;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public TeacherDTO findTeacherById(Long id, Locale locale) {
        return getTeacherDTO(id, locale);
    }

    @Override
    public TeacherDTO findTeacherById(Long id) {
        return getTeacherDTO(id, LocaleContextHolder.getLocale());
    }

    @Override
    public List<TeacherDTO> findAllTeachers() {
        List<Teacher> teachers = repository.findAll();
        return teachers.stream()
            .map(student -> modelMapper.map(student, TeacherDTO.class))
            .toList();
    }

    @Override
    public List<TeacherDTO> findTeachersByFirstName(String firstName) {
        List<Teacher> byFirstNameStartingWith = repository.findAllByFirstNameStartingWith(firstName);

        return byFirstNameStartingWith.stream()
            .map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
            .toList();
    }

    @Transactional
    @Override
    public void saveTeachers(List<TeacherDTO> teacherList) {
        isValidTeachersList(teacherList);

        List<Teacher> teachers = teacherList.stream()
            .map(teacherDTO -> modelMapper.map(teacherDTO, Teacher.class))
            .toList();

        repository.saveAllAndFlush(teachers);
    }

    @Transactional
    @Override
    public void saveTeacher(TeacherDTO teacher, Locale locale) {
        isValidTeacher(teacher, locale);
        repository.save(buildTeacherWithCourses(teacher, locale));
    }

    @Transactional
    @Override
    public void saveTeacher(TeacherDTO teacher) {
        Locale locale = LocaleContextHolder.getLocale();
        isValidTeacher(teacher, locale);
        repository.save(buildTeacherWithCourses(teacher, locale));
    }

    @Transactional
    @Override
    public void updateTeacher(Long id, TeacherDTO updated) {
        Locale locale = LocaleContextHolder.getLocale();
        updateTeacherWithLocale(id, updated, locale);
    }

    @Transactional
    @Override
    public void updateTeacher(Long id, TeacherDTO updated, Locale locale) {
        updateTeacherWithLocale(id, updated, locale);
    }

    @Transactional
    @Override
    public void deleteTeacherById(Long teacherId) {
        repository.findById(teacherId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{teacherId}, "Teacher with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(teacherId);
    }

    private void isValidTeacher(TeacherDTO teacher, Locale locale) {
        if (teacher == null || teacher.getFirstName() == null || teacher.getFirstName().isEmpty() ||
            teacher.getLastName() == null || teacher.getLastName().isEmpty() ||
            teacher.getUsername() == null || teacher.getUsername().isEmpty() ||
            teacher.getPassword() == null || teacher.getPassword().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("teacher.errors.invalid", new Object[0],
                "Invalid teacher data", locale));
        }
    }

    private void isValidTeachersList(List<TeacherDTO> teacherList) {
        Locale locale = LocaleContextHolder.getLocale();
        if (teacherList == null || teacherList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("student.errors.empty_list", new Object[0],
                "Teacher list is null or empty", locale));
        }

        for (TeacherDTO teacher : teacherList) {
            isValidTeacher(teacher, locale);
        }
    }

    private TeacherDTO getTeacherDTO(Long id, Locale locale) {
        return repository.findById(id)
            .map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{id}, "Teacher with ID {0} not found!", locale)));
    }

    private Teacher buildTeacherWithCourses(TeacherDTO teacherDTO, Locale locale) {
        Teacher teacherEntity = modelMapper.map(teacherDTO, Teacher.class);

        List<Integer> courseIds = teacherDTO.getCourseIds();
        Set<Course> newCourses = new HashSet<>();

        if (courseIds != null && !courseIds.isEmpty()) {
            for (Integer courseId : courseIds) {
                Course course = modelMapper.map(courseService.findCourseById(courseId, locale), Course.class);
                newCourses.add(course);
            }
        }

        for (Course course : teacherEntity.getCourses()) {
            if (!newCourses.contains(course)) {
                teacherEntity.removeCourse(course);
            }
        }
        newCourses.forEach(teacherEntity::addCourse);

        return teacherEntity;
    }

    private void updateTeacherWithLocale(Long id, TeacherDTO updated, Locale locale) {
        isValidTeacher(updated, locale);

        Teacher teacherWithCourses = buildTeacherWithCourses(updated, locale);

        repository.findById(id)
            .map(teacher -> {
                teacher.setFirstName(teacherWithCourses.getFirstName());
                teacher.setLastName(teacherWithCourses.getLastName());
                teacher.setUsername(teacherWithCourses.getUsername());
                teacher.setPassword(teacherWithCourses.getPassword());
                teacher.setCourses(teacherWithCourses.getCourses());

                return teacher;
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{id}, "Teacher with ID {0} not found!", locale)));
    }
}
