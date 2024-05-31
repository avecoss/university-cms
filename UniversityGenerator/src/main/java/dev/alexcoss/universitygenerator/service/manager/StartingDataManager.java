package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.*;
import dev.alexcoss.universitygenerator.service.CourseService;
import dev.alexcoss.universitygenerator.service.GroupService;
import dev.alexcoss.universitygenerator.service.StudentService;
import dev.alexcoss.universitygenerator.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartingDataManager {

    private final GroupsManager groupsManager;
    private final CoursesManager coursesManager;
    private final TeachersManager teachersManager;
    private final StudentsManager studentsManager;

    private final GroupService groupService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public UniversityData generateUniversityData(GenerateDataRequest request) {
        List<GGroup> groupList = groupsManager.getGroups(request.getNumberOfGroups());
        List<GTeacher> teacherList = teachersManager.getTeachers(request.getNumberOfTeachers());
        List<GCourse> courseList = coursesManager.getCourses(request.getNumberOfCourses(), teacherList);
        List<GStudent> studentList = studentsManager.getStudents(request.getNumberOfStudents(), groupList, courseList);

        return UniversityData.builder()
            .groups(groupList)
            .courses(courseList)
            .teachers(teacherList)
            .students(studentList)
            .build();
    }

    public UniversityData getUniversityData() {
        return UniversityData.builder()
            .groups(groupService.findAllGroups())
            .courses(courseService.findAllCourses())
            .teachers(teacherService.findAllGTeachers())
            .students(studentService.findAllStudents())
            .build();
    }
}
