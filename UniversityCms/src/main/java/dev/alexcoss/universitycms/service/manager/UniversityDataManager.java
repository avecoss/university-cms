package dev.alexcoss.universitycms.service.manager;

import dev.alexcoss.universitycms.dto.data.UniversityDataResponse;
import dev.alexcoss.universitycms.dto.data.response.GCourse;
import dev.alexcoss.universitycms.dto.data.response.GGroup;
import dev.alexcoss.universitycms.dto.data.response.GStudent;
import dev.alexcoss.universitycms.dto.data.response.GTeacher;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.service.course.GeneratedCourseService;
import dev.alexcoss.universitycms.service.group.GeneratedGroupService;
import dev.alexcoss.universitycms.service.student.GeneratedStudentService;
import dev.alexcoss.universitycms.service.teacher.GeneratedTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UniversityDataManager {

    private final GeneratedStudentService<GStudent> studentService;
    private final GeneratedTeacherService<GTeacher> teacherService;
    private final GeneratedGroupService<GGroup> groupService;
    private final GeneratedCourseService<GCourse> courseService;

    public void saveData(UniversityDataResponse universityData) {
        Map<String, Student> studentMap = convertAndSaveStudents(universityData.getStudents());
        Map<String, Teacher> teacherMap = convertAndSaveTeachers(universityData.getTeachers());
        saveGroups(universityData.getGroups(), studentMap);
        saveCourses(universityData.getCourses(), teacherMap, studentMap);
    }

    private Map<String, Student> convertAndSaveStudents(List<GStudent> gStudents) {
        studentService.saveStudents(gStudents);
        List<Student> students = studentService.findAllStudents();
        return students.stream()
            .collect(Collectors.toMap(Student::getUsername, student -> student));
    }

    private Map<String, Teacher> convertAndSaveTeachers(List<GTeacher> gTeachers) {
        teacherService.saveTeachers(gTeachers);
        List<Teacher> teachers = teacherService.findAllTeachers();

        return teachers.stream()
            .collect(Collectors.toMap(Teacher::getUsername, teacher -> teacher));
    }

    private void saveGroups(List<GGroup> gGroups, Map<String, Student> studentMap) {
        groupService.saveGroups(gGroups, studentMap);
    }

    private void saveCourses(List<GCourse> gCourses, Map<String, Teacher> teacherMap, Map<String, Student> studentMap) {
        courseService.saveCourses(gCourses, teacherMap, studentMap);
    }
}
