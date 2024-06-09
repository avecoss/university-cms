package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.dto.view.teacher.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import dev.alexcoss.universitycms.service.generator.TeacherBuilder;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherProcessingService<TeacherViewDTO, TeacherCreateEditDTO> {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TeacherBuilder teacherBuilder;
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
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
            .map(student -> modelMapper.map(student, TeacherViewDTO.class))
            .toList();
    }

    @Override
    public List<TeacherViewDTO> findTeachersByFirstName(String firstName) {
        List<Teacher> byFirstNameStartingWith = teacherRepository.findAllByFirstNameStartingWith(firstName);

        return byFirstNameStartingWith.stream()
            .map(teacher -> modelMapper.map(teacher, TeacherViewDTO.class))
            .toList();
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveTeacher(TeacherCreateEditDTO teacherDto, Locale locale) {
        isValidTeacher(teacherDto, locale);
        Teacher teacher = teacherBuilder.buildEntity(teacherDto);

        userRepository.save(teacher.getUser());
        teacherRepository.save(teacher);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveTeacher(TeacherCreateEditDTO teacherDto) {
        Locale locale = LocaleContextHolder.getLocale();
        isValidTeacher(teacherDto, locale);
        Teacher teacher = teacherBuilder.buildEntity(teacherDto);

        userRepository.save(teacher.getUser());
        teacherRepository.save(teacher);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTeacher(Long id, TeacherCreateEditDTO updated) {
        updated(id, updated, LocaleContextHolder.getLocale());
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTeacher(Long id, TeacherCreateEditDTO updated, Locale locale) {
        updated(id, updated, locale);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacherById(Long teacherId) {
        teacherRepository.findById(teacherId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{teacherId}, "Teacher with ID {0} not found!", LocaleContextHolder.getLocale())));

        teacherRepository.deleteById(teacherId);
    }

    private void isValidTeacher(TeacherCreateEditDTO teacher, Locale locale) {
        if (teacher == null || teacher.getUser() == null ||
            StringUtils.isBlank(teacher.getUser().getFirstName()) || StringUtils.isBlank(teacher.getUser().getLastName())) {
            throw new IllegalEntityException(messageSource.getMessage("teacher.errors.invalid", new Object[0],
                "Invalid teacher data", locale));
        }
    }

    private TeacherViewDTO getTeacherDTO(Long id, Locale locale) {
        return teacherRepository.findById(id)
            .map(teacher -> modelMapper.map(teacher, TeacherViewDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{id}, "Teacher with ID {0} not found!", locale)));
    }

    private void updated(Long id, TeacherCreateEditDTO updated, Locale locale) {
        isValidTeacher(updated, locale);

        Teacher teacherWithCourses = teacherBuilder.buildEntity(updated);

        teacherRepository.findById(id)
            .map(teacher -> {
                teacher.getUser().setFirstName(teacherWithCourses.getUser().getFirstName());
                teacher.getUser().setLastName(teacherWithCourses.getUser().getLastName());
                teacher.setCourses(teacherWithCourses.getCourses());

                return teacher;
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{id}, "Teacher with ID {0} not found!", locale)));
    }
}
