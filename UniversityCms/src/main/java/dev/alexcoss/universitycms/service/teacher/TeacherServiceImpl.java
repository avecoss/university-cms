package dev.alexcoss.universitycms.service.teacher;

import dev.alexcoss.universitycms.dto.view.users.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.users.TeacherViewDTO;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.service.generator.LoginPasswordGenerator;
import dev.alexcoss.universitycms.service.generator.PersonBuilder;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherProcessingService<TeacherViewDTO, TeacherCreateEditDTO> {

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
    @PreAuthorize("hasRole('ADMIN')")
    public void saveTeacher(TeacherCreateEditDTO teacher, Locale locale) {
        isValidTeacher(teacher, locale);
        repository.save(buildTeacherWithLoginAndPass(teacher));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveTeacher(TeacherCreateEditDTO teacher) {
        Locale locale = LocaleContextHolder.getLocale();
        isValidTeacher(teacher, locale);
        repository.save(buildTeacherWithLoginAndPass(teacher));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTeacher(Long id, TeacherCreateEditDTO updated) {
        updateTeacherFromDto(id, updated, LocaleContextHolder.getLocale());
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTeacher(Long id, TeacherCreateEditDTO updated, Locale locale) {
        updateTeacherFromDto(id, updated, locale);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacherById(Long teacherId) {
        repository.findById(teacherId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                new Object[]{teacherId}, "Teacher with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(teacherId);
    }

    public boolean findPersonByUsername(String username){
        return repository.findByUsername(username).isPresent();
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
