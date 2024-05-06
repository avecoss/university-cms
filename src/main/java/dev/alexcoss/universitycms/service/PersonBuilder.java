package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.dto.users.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.users.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonBuilder {

    private final ModelMapper modelMapper;
    private final CourseService<CourseDTO> courseService;
    private final GroupService<GroupDTO> groupService;

    public Student buildEntity(StudentEditCreateDTO dto) {
        Student entity = modelMapper.map(dto, Student.class);
        updateCourses(entity, dto.getCourseIds());
        updateGroup(entity, dto.getGroupId());
        return entity;
    }

    public Teacher buildEntity(TeacherCreateEditDTO dto) {
        Teacher entity = modelMapper.map(dto, Teacher.class);
        updateCourses(entity, dto.getCourseIds());
        return entity;
    }

    private void updateCourses(Person entity, List<Integer> courseIds) {
        List<CourseDTO> coursesDTO = courseService.findAllByIds(courseIds);
        Set<Course> newCourses = coursesDTO.stream()
            .map(courseDTO -> modelMapper.map(courseDTO, Course.class))
            .collect(Collectors.toSet());

        if (entity instanceof Student student) {
            student.setRole(Role.STUDENT);
            if (student.getCourses() != null || !student.getCourses().isEmpty()) {
                for (Course course : student.getCourses()) {
                    if (!newCourses.contains(course)) {
                        student.removeCourse(course);
                    }
                }
            }
            newCourses.forEach(student::addCourse);
        } else if (entity instanceof Teacher teacher) {
            teacher.setRole(Role.TEACHER);
            if (teacher.getCourses() != null || !teacher.getCourses().isEmpty()) {
                for (Course course : teacher.getCourses()) {
                    if (!newCourses.contains(course)) {
                        teacher.removeCourse(course);
                    }
                }
            }
            newCourses.forEach(teacher::addCourse);
        }
    }

    private void updateGroup(Student student, Integer groupId) {
        if (groupId != null) {
            Group group = modelMapper.map(groupService.findGroupById(groupId), Group.class);
            Group oldGroup = student.getGroup();
            if (oldGroup != null) {
                student.removeGroup(oldGroup);
            }
            student.setGroup(group);
        }
    }
}
