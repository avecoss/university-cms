package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TeacherServiceImpl.class, ModelMapper.class})
class TeacherServiceImplTest {

    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private CourseService<CourseDTO> courseService;

    @Autowired
    private TeacherServiceImpl teacherService;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    void testFindTeacherById() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        TeacherDTO teacherDTO = teacherService.findTeacherById(id);

        assertNotNull(teacherDTO);
        assertEquals(id, teacherDTO.getId());
    }

    @Test
    void testNotFoundTeacherById() {
        Long id = 9L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> teacherService.findTeacherById(id));
    }

    @Test
    void testSaveTeacher() {
        when(courseService.findCourseById(1, Locale.getDefault())).thenReturn(any(CourseDTO.class));
        when(courseService.findCourseById(2, Locale.getDefault())).thenReturn(any(CourseDTO.class));

        teacherService.saveTeacher(any(TeacherDTO.class));

        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testBuildTeacherWithCourses() {
        TeacherDTO teacherDTO = TeacherDTO.builder().id(1L).courseIds(Arrays.asList(1, 2, 3)).build();

        CourseDTO course1 = CourseDTO.builder().id(1).build();
        CourseDTO course2 = CourseDTO.builder().id(2).build();

        when(courseService.findCourseById(1, Locale.getDefault())).thenReturn(course1);
        when(courseService.findCourseById(2, Locale.getDefault())).thenReturn(course2);

        Teacher teacherEntity = ReflectionTestUtils.invokeMethod(teacherService, "buildTeacherWithCourses", teacherDTO, Locale.getDefault());

        assertNotNull(teacherEntity);
        assertEquals(teacherDTO.getId(), teacherEntity.getId());
        assertEquals(2, teacherEntity.getCourses().size());
        assertTrue(teacherEntity.getCourses().contains(modelMapper.map(course1, Course.class)));
        assertTrue(teacherEntity.getCourses().contains(modelMapper.map(course2, Course.class)));
    }
}