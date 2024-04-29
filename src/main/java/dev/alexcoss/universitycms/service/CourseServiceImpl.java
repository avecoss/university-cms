package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.CourseRepository;
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
public class CourseServiceImpl implements CourseService<CourseDTO> {

    private final CourseRepository repository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public List<CourseDTO> findAllCourses() {
        List<Course> courses = repository.findAll();

        return courses.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();
    }

    @Override
    public List<CourseDTO> findCoursesByLetters(String letter) {
        List<Course> allByNameStartingWith = repository.findAllByNameStartingWith(letter);

        return allByNameStartingWith.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();

    }

    @Override
    public Optional<CourseDTO> findCourseById(Integer id) {
        return repository.findById(id)
            .map(course -> modelMapper.map(course, CourseDTO.class));
    }

    @Transactional
    @Override
    public void saveCourses(List<CourseDTO> courseList) {
        isValidCourseList(courseList);

        List<Course> courses = courseList.stream()
            .map(courseDTO -> modelMapper.map(courseDTO, Course.class))
            .toList();

        repository.saveAllAndFlush(courses);

    }

    @Transactional
    @Override
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
    public void saveCourse(CourseDTO courseDTO) {
        isValidCourse(courseDTO);
        repository.save(modelMapper.map(courseDTO, Course.class));
    }

    @Transactional
    @Override
    public void deleteCourseById(Integer courseId) {
        repository.findById(courseId)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("course.errors.not_found",
                new Object[] {courseId}, "Course with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(courseId);
    }

    private void isValidCourseList(List<CourseDTO> courseList) {
        if (courseList == null || courseList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("course.errors.empty_list", new Object[0],
                "Course list is null or empty", LocaleContextHolder.getLocale()));
        }

        for (CourseDTO course : courseList) {
            isValidCourse(course);
        }
    }

    private void isValidCourse(CourseDTO course) {
        if (course == null ||
            course.getName() == null || course.getName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("course.errors.invalid", new Object[0],
                "Invalid course data", LocaleContextHolder.getLocale()));
        }
    }
}
