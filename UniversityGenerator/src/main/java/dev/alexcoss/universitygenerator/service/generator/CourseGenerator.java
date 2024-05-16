package dev.alexcoss.universitygenerator.service.generator;

import dev.alexcoss.universitygenerator.dto.CourseDTO;
import dev.alexcoss.universitygenerator.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseGenerator {

    private final CourseService courseService;

    @Value("${data.course.amount}")
    private int courseAmount;

    public List<CourseDTO> generateCourseList(int count) {
        if (count > 0)
            courseAmount = count;

        List<CourseDTO> courses = new ArrayList<>(courseService.findAllCourses());

        for (int i = courses.size(); i < courseAmount; i++) {
            String newName = "Course " + (i + 1);
            CourseDTO newCourse = CourseDTO.builder()
                .name(newName)
                .build();
            courses.add(newCourse);
        }
        return courses;
    }
}
