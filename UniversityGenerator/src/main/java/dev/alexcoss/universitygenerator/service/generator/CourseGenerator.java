package dev.alexcoss.universitygenerator.service.generator;

import dev.alexcoss.universitygenerator.dto.GCourse;
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

    public List<GCourse> generateCourseList(int count) {
        if (count > 0)
            courseAmount = count;

        List<GCourse> existingCourses = new ArrayList<>(courseService.findAllCourses());
        for (var course : existingCourses) {
            course.getStudentUsernames().clear();
        }
        int existingCoursesCount = existingCourses.size();
        if (existingCoursesCount >= courseAmount) {
            return existingCourses.subList(0, courseAmount);
        }

        int coursesToGenerate = courseAmount - existingCoursesCount;
        List<GCourse> courses = new ArrayList<>(existingCourses);

        for (int i = coursesToGenerate; i < courseAmount; i++) {
            String newName = "Course " + (i + 1);
            GCourse newCourse = GCourse.builder()
                .name(newName)
                .build();
            courses.add(newCourse);
        }
        return courses;
    }
}
