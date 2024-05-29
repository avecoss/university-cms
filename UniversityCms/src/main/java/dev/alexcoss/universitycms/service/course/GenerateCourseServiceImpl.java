package dev.alexcoss.universitycms.service.course;

import dev.alexcoss.universitycms.dto.data.response.GCourse;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.CourseRepository;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateCourseServiceImpl implements GeneratedCourseService<GCourse> {

    private final CourseRepository repository;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public void saveCourses(List<GCourse> gCourses, Map<String, Teacher> teacherMap, Map<String, Student> studentMap) {
        isValidCourseList(gCourses);

        List<Course> courseList = new ArrayList<>();
        for (GCourse gCourse : gCourses) {
            Course course = new Course();
            course.setName(gCourse.getName());

            Teacher teacher = teacherMap.get(gCourse.getTeacherUsername());
            course.addTeacher(teacher);

            for (String studentUsername : gCourse.getStudentUsernames()) {
                course.addStudent(studentMap.get(studentUsername));
            }
            courseList.add(course);
        }

        repository.saveAllAndFlush(courseList);
    }


    private void isValidCourseList(List<GCourse> courseList) {
        if (courseList == null || courseList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("course.errors.empty_list", new Object[0],
                "Course list is null or empty", LocaleContextHolder.getLocale()));
        }

        for (GCourse course : courseList) {
            isValidCourse(course);
        }
    }

    private void isValidCourse(GCourse course) {
        if (course == null ||
            course.getName() == null || course.getName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("course.errors.invalid", new Object[0],
                "Invalid course data", LocaleContextHolder.getLocale()));
        }
    }
}
