package dev.alexcoss.universitycms.service.generator;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.*;
import dev.alexcoss.universitycms.service.course.CourseProcessingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeacherBuilder implements EntityBuilder<Teacher, TeacherCreateEditDTO> {

    private final ModelMapper modelMapper;
    private final CourseProcessingService<CourseDTO> courseService;
    private final UserFactory userFactory;

    @Override
    public Teacher buildEntity(TeacherCreateEditDTO dto) {
        UserDTO userDTO = dto.getUser();
        User user = userFactory.createUser(userDTO, Role.TEACHER);

        Teacher entity = modelMapper.map(dto, Teacher.class);
        entity.setUser(user);

        updateCourses(entity, dto.getCourseIds());
        return entity;
    }

    private void updateCourses(Teacher teacher, List<Integer> courseIds) {
        List<CourseDTO> coursesDTO = courseService.findAllByIds(courseIds);
        Set<Course> newCourses = coursesDTO.stream()
            .map(courseDTO -> modelMapper.map(courseDTO, Course.class))
            .collect(Collectors.toSet());

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

