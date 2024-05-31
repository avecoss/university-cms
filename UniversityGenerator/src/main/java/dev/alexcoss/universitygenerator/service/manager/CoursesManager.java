package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.GCourse;
import dev.alexcoss.universitygenerator.dto.GTeacher;
import dev.alexcoss.universitygenerator.service.generator.CourseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoursesManager {

    private final CourseGenerator courseGenerator;

    public List<GCourse> getCourses(int amount, List<GTeacher> teachers) {
        List<GCourse> courses = courseGenerator.generateCourseList(amount);
        if (teachers == null || teachers.isEmpty())
            return courses;

        for (int i = 0; i < courses.size() && i < teachers.size(); i++) {
            courses.get(i).setTeacherUsername(teachers.get(i).getUsername());
        }

        return courses;
    }
}
