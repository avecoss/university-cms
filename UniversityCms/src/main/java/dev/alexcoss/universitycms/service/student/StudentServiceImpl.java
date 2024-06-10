package dev.alexcoss.universitycms.service.student;

import dev.alexcoss.universitycms.dto.view.student.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentViewDTO;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import dev.alexcoss.universitycms.service.generator.StudentBuilder;
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
public class StudentServiceImpl implements StudentService<StudentViewDTO, StudentEditCreateDTO> {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentBuilder studentBuilder;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public StudentViewDTO getStudentById(Long id) {
        return getStudentDTO(id, LocaleContextHolder.getLocale());
    }

    @Override
    public StudentViewDTO getStudentById(Long id, Locale locale) {
        return getStudentDTO(id, locale);
    }

    @Override
    public List<StudentViewDTO> getStudentsByCourse(String courseName) {
        List<Student> students = studentRepository.findByCoursesName(courseName);
        return students.stream()
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .toList();
    }

    @Override
    public List<StudentViewDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .toList();
    }

    @Override
    public List<StudentViewDTO> getStudentsByFirstName(String name) {
        List<Student> byFirstNameStartingWith = studentRepository.findAllByFirstNameStartingWith(name);

        return byFirstNameStartingWith.stream()
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .toList();
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveStudent(StudentEditCreateDTO studentDto) {
        Locale locale = LocaleContextHolder.getLocale();
        isValidStudent(studentDto, locale);
        Student student = studentBuilder.buildEntity(studentDto);

        userRepository.save(student.getUser());
        studentRepository.save(student);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveStudent(StudentEditCreateDTO studentDto, Locale locale) {
        isValidStudent(studentDto, locale);
        Student student = studentBuilder.buildEntity(studentDto);

        userRepository.save(student.getUser());
        studentRepository.save(student);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateStudent(Long id, StudentEditCreateDTO updated) {
        applyEntityUpdates(id, updated, LocaleContextHolder.getLocale());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void updateStudent(Long id, StudentEditCreateDTO updated, Locale locale) {
        applyEntityUpdates(id, updated, locale);
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudentById(Long studentId) {
        studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{studentId}, "Student with ID {0} not found!", LocaleContextHolder.getLocale())));

        studentRepository.deleteById(studentId);
    }

    private void isValidStudent(StudentEditCreateDTO student, Locale locale) {
        if (student == null || student.getUser() == null ||
            StringUtils.isBlank(student.getUser().getFirstName()) || StringUtils.isBlank(student.getUser().getLastName())) {
            throw new IllegalEntityException(messageSource.getMessage("student.errors.invalid", new Object[0],
                "Invalid student data", locale));
        }
    }

    private StudentViewDTO getStudentDTO(Long id, Locale locale) {
        return studentRepository.findById(id)
            .map(student -> modelMapper.map(student, StudentViewDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{id}, "Student with ID {0} not found!", locale)));
    }

    private void applyEntityUpdates(Long id, StudentEditCreateDTO updated, Locale locale) {
        isValidStudent(updated, locale);

        Student buildStudent = studentBuilder.buildEntity(updated);

        studentRepository.findById(id)
            .map(student -> {
                student.getUser().setFirstName(buildStudent.getUser().getFirstName());
                student.getUser().setLastName(buildStudent.getUser().getLastName());
                student.setCourses(buildStudent.getCourses());
                student.setGroup(buildStudent.getGroup());

                return student;
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{id}, "Student with ID {0} not found!", locale)));
    }
}
