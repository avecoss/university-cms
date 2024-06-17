package dev.alexcoss.universitycms.controller.teacher;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.teacher.TeacherService;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService<TeacherViewDTO, TeacherCreateEditDTO> teacherService;

    @MockBean
    private CourseService<CourseDTO> courseService;

    @Test
    @WithMockUser
    public void testTeacherDetails() throws Exception {
        long teacherId = 1L;
        Locale locale = Locale.ENGLISH;
        TeacherViewDTO teacherViewDTO = TeacherViewDTO.builder().id(teacherId).user(getUserDto()).courses(List.of()).build();

        when(teacherService.getTeacherById(teacherId, locale)).thenReturn(teacherViewDTO);

        mockMvc.perform(get("/teachers/{id}", teacherId))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/t_details"))
            .andExpect(model().attribute("teacherView", teacherViewDTO));
    }

    @Test
    @WithMockUser
    public void testTeacherDetailsNotFound() throws Exception {
        long teacherId = 1L;
        Locale locale = Locale.ENGLISH;

        doThrow(EntityNotExistException.class).when(teacherService).getTeacherById(teacherId, locale);

        mockMvc.perform(get("/teachers/{id}", teacherId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testEditTeacher() throws Exception {
        long teacherId = 1L;
        Locale locale = Locale.ENGLISH;
        TeacherViewDTO teacherViewDTO = TeacherViewDTO.builder().id(teacherId).user(getUserDto()).courses(List.of()).build();
        List<CourseDTO> courseDTOs = List.of();

        when(teacherService.getTeacherById(teacherId, locale)).thenReturn(teacherViewDTO);
        when(courseService.getAllCourses()).thenReturn(courseDTOs);

        mockMvc.perform(get("/teachers/{id}/edit", teacherId))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/t_edit"))
            .andExpect(model().attribute("teacherView", teacherViewDTO))
            .andExpect(model().attribute("courses", courseDTOs));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateTeacher() throws Exception {
        long teacherId = 1L;
        TeacherCreateEditDTO teacherEditCreateDTO = TeacherCreateEditDTO.builder().id(teacherId).user(getUserDto()).courseIds(List.of(1, 2)).build();

        mockMvc.perform(patch("/teachers/{id}", teacherId)
                .flashAttr("teacher", teacherEditCreateDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/teachers"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteTeacher() throws Exception {
        long teacherId = 1L;

        mockMvc.perform(delete("/teachers/{id}", teacherId).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).deleteTeacherById(teacherId);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteNonExistingTeacher() throws Exception {
        long teacherId = 1L;

        doThrow(EntityNotExistException.class).when(teacherService).deleteTeacherById(teacherId);

        mockMvc.perform(delete("/teachers/{id}", teacherId).with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    private UserDTO getUserDto() {
        return UserDTO.builder()
            .firstName("teacher")
            .lastName("teacher")
            .authorities(List.of(AuthorityDTO.builder().role(Role.TEACHER).build()))
            .build();
    }
}