package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository repository;
    private final ModelMapper modelMapper;

    public List<CourseDTO> getCourses() {
        List<Course> courses = repository.findAll();

        return courses.stream()
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();
    }

    @Transactional
    public void addCourses(List<CourseDTO> courseList) {
        if (isValidCourseList(courseList)) {
            List<Course> courses = courseList.stream()
                .map(courseDTO -> modelMapper.map(courseDTO, Course.class))
                .toList();

            repository.saveAllAndFlush(courses);
        }
    }

    @Transactional
    public void addCourse(CourseDTO courseDTO) {
        if (isValidCourse(courseDTO)) {
            repository.save(modelMapper.map(courseDTO, Course.class));
        }
    }

    private boolean isValidCourseList(List<CourseDTO> courseList) {
        if (courseList == null || courseList.isEmpty()) {
            log.error("Course list is null or empty");
            return false;
        }

        for (CourseDTO course : courseList) {
            if (!isValidCourse(course))
                return false;
        }
        return true;
    }

    private boolean isValidCourse(CourseDTO course) {
        if (course == null) {
            log.error("Invalid course in the list");
            return false;
        }
        return true;
    }
}
