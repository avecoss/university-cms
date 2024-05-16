package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.CourseDTO;
import dev.alexcoss.universitygenerator.dto.TeacherDTO;
import dev.alexcoss.universitygenerator.service.generator.CourseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoursesManager {

    private final CourseGenerator courseGenerator;

    public List<CourseDTO> getCourses(int amount, List<TeacherDTO> teachers) {
        List<CourseDTO> courses = courseGenerator.generateCourseList(amount);
        if (teachers == null || teachers.isEmpty())
            return courses;

        for (int i = 0; i < courses.size() && i < teachers.size(); i++) {
            courses.get(i).setTeacher(teachers.get(i));
            teachers.get(i).getCourses().add(courses.get(i));
        }
        return courses;
    }
}
