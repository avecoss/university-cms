package dev.alexcoss.universitygenerator.service;

import dev.alexcoss.universitygenerator.dto.CourseDTO;
import dev.alexcoss.universitygenerator.model.Course;
import dev.alexcoss.universitygenerator.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService{

    private final CourseRepository repository;
    private final ModelMapper modelMapper;

    public List<CourseDTO> findAllCourses() {
        Iterable<Course> courses = repository.findAll();

        return StreamSupport.stream(courses.spliterator(), false)
            .map(course -> modelMapper.map(course, CourseDTO.class))
            .toList();
    }
}
