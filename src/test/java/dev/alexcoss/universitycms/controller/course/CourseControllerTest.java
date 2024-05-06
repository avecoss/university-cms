package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.users.TeacherViewDTO;
import dev.alexcoss.universitycms.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    public void testCourseDetails() throws Exception {
        int courseId = 1;
        CourseDTO courseDTO = CourseDTO.builder().id(courseId).name("Mathematics").teacher(new TeacherViewDTO()).build();;

        when(courseService.findCourseById(courseId)).thenReturn(courseDTO);

        mockMvc.perform(get("/courses/{id}", courseId))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_details"))
            .andExpect(model().attribute("course", courseDTO));
    }

    @Test
    public void testCourseDetailsNotFound() throws Exception {
        int courseId = 1;

        doThrow(EntityNotExistException.class).when(courseService).findCourseById(1);

        mockMvc.perform(get("/courses/{id}", courseId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    @Test
    public void testEditCourse() throws Exception {
        int courseId = 1;
        CourseDTO courseDTO = CourseDTO.builder().id(courseId).name("Mathematics").teacher(new TeacherViewDTO()).build();

        when(courseService.findCourseById(courseId)).thenReturn(courseDTO);
        when(teacherService.findAllTeachers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/courses/{id}/edit", courseId))
            .andExpect(status().isOk())
            .andExpect(view().name("courses/c_edit"))
            .andExpect(model().attribute("course", courseDTO))
            .andExpect(model().attributeExists("teachers"));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        int courseId = 1;
        CourseDTO courseDTO = CourseDTO.builder().id(courseId).name("Mathematics").teacher(new TeacherViewDTO()).build();
        TeacherViewDTO teacherDTO = TeacherViewDTO.builder().id(1).build();

        when(teacherService.findTeacherById(1L)).thenReturn(teacherDTO);

        mockMvc.perform(patch("/courses/{id}", courseId)
                .param("teacherId", "1")
                .flashAttr("course", courseDTO))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        int courseId = 1;

        mockMvc.perform(delete("/courses/{id}", courseId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).deleteCourseById(courseId);
    }
}