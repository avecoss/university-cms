package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.teacher.TeacherServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CoursesController.class)
class CoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService<CourseDTO> courseService;

    @MockBean
    private TeacherServiceImpl teacherService;

    @Test
    @WithMockUser
    public void testCourses() throws Exception {
        List<CourseDTO> courses = new ArrayList<>();
        courses.add(new CourseDTO(1, "Mathematics", getTeacherDto(1L)));
        courses.add(new CourseDTO(2, "Physics", getTeacherDto(2L)));

        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/courses"))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_list"))
            .andExpect(model().attribute("courses", courses));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testNewCourse() throws Exception {
        List<TeacherViewDTO> teachers = List.of(getTeacherDto(1L), getTeacherDto(2L));

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/courses/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_new"))
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attribute("teachers", teachers));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateCourse() throws Exception {
        Long teacherId = 1L;
        TeacherViewDTO teacherDTO = getTeacherDto(teacherId);
        CourseDTO courseDTO = CourseDTO.builder().name("Mathematics").teacher(teacherDTO).build();

        when(teacherService.getTeacherById(teacherId)).thenReturn(teacherDTO);

        mockMvc.perform(post("/courses")
                .param("teacherId", teacherId.toString())
                .flashAttr("course", courseDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).saveCourse(any(CourseDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateCourseValidationFailure() throws Exception {
        CourseDTO invalidCourseDTO = CourseDTO.builder().id(1).name("").teacher(new TeacherViewDTO()).build();

        when(teacherService.getAllTeachers()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/courses")
                .param("teacherId", "1")
                .flashAttr("course", invalidCourseDTO)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_new"))
            .andExpect(model().attributeExists("course"))
            .andExpect(model().attribute("teachers", Collections.emptyList()))
            .andExpect(model().hasErrors());

        verify(teacherService, times(1)).getAllTeachers();
        verify(courseService, times(0)).saveCourse(any(CourseDTO.class));
    }

    private TeacherViewDTO getTeacherDto(@NotNull Long teacherId) {
        return TeacherViewDTO.builder()
            .id(teacherId)
            .user(getUserDto(teacherId))
            .courses(List.of())
            .build();
    }

    private UserDTO getUserDto(@NotNull Long teacherId) {
        return UserDTO.builder()
            .firstName("teacher" + teacherId)
            .lastName("teacher" + teacherId)
            .authorities(List.of(AuthorityDTO.builder().role(Role.TEACHER).build()))
            .build();
    }
}