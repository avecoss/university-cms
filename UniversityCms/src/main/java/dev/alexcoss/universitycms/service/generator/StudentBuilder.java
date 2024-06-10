package dev.alexcoss.universitycms.service.generator;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.*;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentBuilder implements EntityBuilder<Student, StudentEditCreateDTO> {

    private final ModelMapper modelMapper;
    private final CourseService<CourseDTO> courseService;
    private final GroupService<GroupDTO> groupService;
    private final UserFactory userFactory;

    @Override
    public Student buildEntity(StudentEditCreateDTO dto) {
        UserDTO userDTO = dto.getUser();
        User user = userFactory.createUser(userDTO, Role.STUDENT);

        Student entity = modelMapper.map(dto, Student.class);
        entity.setUser(user);

        updateCourses(entity, dto.getCourseIds());
        updateGroup(entity, dto.getGroupId());
        return entity;
    }

    private void updateCourses(Student student, List<Integer> courseIds) {
        List<CourseDTO> coursesDTO = courseService.getAllByIds(courseIds);
        Set<Course> newCourses = coursesDTO.stream()
            .map(courseDTO -> modelMapper.map(courseDTO, Course.class))
            .collect(Collectors.toSet());

        if (student.getCourses() != null || !student.getCourses().isEmpty()) {
            student.getCourses().removeIf(course -> !newCourses.contains(course));
        }
        newCourses.forEach(student::addCourse);
    }

    private void updateGroup(Student student, Integer groupId) {
        if (groupId != null) {
            Group group = modelMapper.map(groupService.getGroupById(groupId), Group.class);
            Group oldGroup = student.getGroup();
            if (oldGroup != null) {
                student.removeGroup(oldGroup);
            }
            student.setGroup(group);
        }
    }
}
