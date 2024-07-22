package dev.alexcoss.universitygenerator.service;

import dev.alexcoss.universitygenerator.dto.GCourse;
import dev.alexcoss.universitygenerator.mapper.CourseMapper;
import dev.alexcoss.universitygenerator.model.Course;
import dev.alexcoss.universitygenerator.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService{

    private final CourseRepository repository;
    private final CourseMapper courseMapper;

    public List<GCourse> findAllCourses() {
        Iterable<Course> courses = repository.findAll();

        return StreamSupport.stream(courses.spliterator(), false)
            .map(courseMapper::courseToGCourse)
            .toList();
    }
}
