package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.service.course.CourseProcessingService;
import dev.alexcoss.universitycms.service.teacher.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CoursesController.class)
class CoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseProcessingService<CourseDTO> courseService;

    @MockBean
    private TeacherServiceImpl teacherService;

    @Test
    public void testCourses() throws Exception {
        List<CourseDTO> courses = new ArrayList<>();
        courses.add(new CourseDTO(1, "Mathematics", new TeacherViewDTO()));
        courses.add(new CourseDTO(2, "Physics", new TeacherViewDTO()));

        when(courseService.findAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/courses"))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_list"))
            .andExpect(model().attribute("courses", courses));
    }

    @Test
    public void testNewCourse() throws Exception {
        List<TeacherViewDTO> teachers = new ArrayList<>();
        teachers.add(new TeacherViewDTO());
        teachers.add(new TeacherViewDTO());

        when(teacherService.findAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/courses/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_new"))
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attribute("teachers", teachers));
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(1, "Mathematics", new TeacherViewDTO());

        when(teacherService.findTeacherById(1L)).thenReturn(new TeacherViewDTO());

        mockMvc.perform(post("/courses")
                .param("teacherId", "1")
                .param("name", "Mathematics"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).saveCourse(any(CourseDTO.class));
    }

    @Test
    public void testCreateCourseValidationFailure() throws Exception {
        CourseDTO invalidCourseDTO = new CourseDTO(1, "", new TeacherViewDTO());

        mockMvc.perform(post("/courses")
                .param("teacherId", "1")
                .param("name", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_new"))
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attribute("teachers", teacherService.findAllTeachers()))
            .andExpect(model().hasErrors());
    }
}