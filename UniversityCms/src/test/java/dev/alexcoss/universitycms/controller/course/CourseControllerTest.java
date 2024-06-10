package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.teacher.TeacherServiceImpl;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService<CourseDTO> courseService;

    @MockBean
    private TeacherServiceImpl teacherService;

    @Test
    @WithMockUser
    public void testCourseDetails() throws Exception {
        int courseId = 1;
        CourseDTO courseDTO = CourseDTO.builder().id(courseId).name("Mathematics").teacher(new TeacherViewDTO()).build();;

        when(courseService.getCourseById(courseId)).thenReturn(courseDTO);

        mockMvc.perform(get("/courses/{id}", courseId))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_details"))
            .andExpect(model().attribute("course", courseDTO));
    }

    @Test
    @WithMockUser
    public void testCourseDetailsNotFound() throws Exception {
        int courseId = 1;

        doThrow(EntityNotExistException.class).when(courseService).getCourseById(1);

        mockMvc.perform(get("/courses/{id}", courseId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testEditCourse() throws Exception {
        int courseId = 1;
        CourseDTO courseDTO = CourseDTO.builder().id(courseId).name("Mathematics").teacher(new TeacherViewDTO()).build();

        when(courseService.getCourseById(courseId)).thenReturn(courseDTO);
        when(teacherService.getAllTeachers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/courses/{id}/edit", courseId))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_edit"))
            .andExpect(model().attribute("course", courseDTO))
            .andExpect(model().attributeExists("teachers"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateCourse() throws Exception {
        int courseId = 1;
        CourseDTO courseDTO = CourseDTO.builder().id(courseId).name("Mathematics").teacher(new TeacherViewDTO()).build();
        TeacherViewDTO teacherDTO = TeacherViewDTO.builder().id(1L).build();

        when(teacherService.getTeacherById(1L)).thenReturn(teacherDTO);

        mockMvc.perform(patch("/courses/{id}", courseId)
                .param("teacherId", "1")
                .flashAttr("course", courseDTO))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteCourse() throws Exception {
        int courseId = 1;

        mockMvc.perform(delete("/courses/{id}", courseId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).deleteCourseById(courseId);
    }
}