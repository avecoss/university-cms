package dev.alexcoss.universitycms.service.course;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.CourseRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService<CourseDTO> {

    private final CourseRepository repository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = repository.findAll();

        return courses.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();
    }

    @Override
    public List<CourseDTO> getCoursesByLetters(String letter) {
        List<Course> allByNameStartingWith = repository.findAllByNameStartingWith(letter);

        return allByNameStartingWith.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();
    }

    @Override
    public CourseDTO getCourseById(Integer id, Locale locale) {
        return getCourseDTO(id, locale);
    }

    @Override
    public CourseDTO getCourseById(Integer id) {
        return getCourseDTO(id, LocaleContextHolder.getLocale());
    }

    @Override
    public List<CourseDTO> getAllByIds(Iterable<Integer> ids) {
        List<Course> coursesByIds = repository.findAllById(ids);

        return coursesByIds.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCourse(Integer courseId, CourseDTO updatedCourse) {
        isValidCourse(updatedCourse);

        repository.findById(courseId)
            .map(course -> {
                course.setName(updatedCourse.getName());
                course.setTeacher(modelMapper.map(updatedCourse.getTeacher(), Teacher.class));
                return repository.save(course);
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("course.errors.not_found",
                new Object[] {courseId}, "Course with ID {0} not found!", LocaleContextHolder.getLocale())));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveCourse(CourseDTO courseDTO) {
        isValidCourse(courseDTO);
        repository.save(modelMapper.map(courseDTO, Course.class));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourseById(Integer courseId) {
        repository.findById(courseId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("course.errors.not_found",
                new Object[] {courseId}, "Course with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(courseId);
    }

    private void isValidCourse(CourseDTO course) {
        if (course == null ||
            course.getName() == null || course.getName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("course.errors.invalid", new Object[0],
                "Invalid course data", LocaleContextHolder.getLocale()));
        }
    }

    private CourseDTO getCourseDTO(Integer id, Locale locale) {
        return repository.findById(id)
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("course.errors.not_found",
                new Object[]{id}, "Course with ID {0} not found!", locale)));
    }
}
