package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.CourseRepository;
import dev.alexcoss.universitycms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentCourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void addAllStudentCourseRelationships(Map<Long, Set<Integer>> studentCourseMap) {
        if (isValidStudentCourseMap(studentCourseMap)) {
            studentCourseMap.forEach((studentId, courseIds) -> {
                studentRepository.findById(studentId).ifPresent(student -> {
                    courseIds.stream()
                        .map(courseRepository::findById)
                        .flatMap(Optional::stream)
                        .forEach(course -> {
                            course.addStudentToCourse(student);
                        });
                });
            });
        }
    }

    @Transactional
    public void addStudentToCourse(long studentId, int courseId) {
        if (isValidId(studentId, "Student ID") && isValidId(courseId, "Course ID")) {
            Optional<Student> studentById = studentRepository.findById(studentId);
            Optional<Course> courseById = courseRepository.findById(courseId);

            if (studentById.isPresent() && courseById.isPresent()) {
                courseById.get().addStudentToCourse(studentById.get());
            }
        }
    }

    @Transactional
    public void removeStudentFromCourse(long studentId, int courseId) {
        if (isValidId(studentId, "Student ID") && isValidId(courseId, "Course ID")) {
            Optional<Student> studentById = studentRepository.findById(studentId);
            Optional<Course> courseById = courseRepository.findById(courseId);

            if (studentById.isPresent() && courseById.isPresent()) {
                courseById.get().removeStudentFromCourse(studentById.get());
            }
        }
    }

    private boolean isValidStudentCourseMap(Map<Long, Set<Integer>> studentCourseMap) {
        if (studentCourseMap == null || studentCourseMap.isEmpty()) {
            log.error("Student-course map is null or empty");
            return false;
        }
        studentCourseMap.forEach((studentId, courseSet) -> {
            if (!isValidId(studentId, "Student ID")) {
                return;
            }
            if (courseSet == null || courseSet.isEmpty()) {
                log.error("Course set for student ID {} is null or empty", studentId);
                return;
            }
            courseSet.forEach(courseId -> isValidId(courseId, "Course ID"));
        });
        return true;
    }

    private boolean isValidId(long id, String idName) {
        if (id <= 0) {
            log.error("{} is negative integer", idName);
            return false;
        }
        return true;
    }
}
