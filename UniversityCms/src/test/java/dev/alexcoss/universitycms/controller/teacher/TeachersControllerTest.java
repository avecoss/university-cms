package dev.alexcoss.universitycms.controller.teacher;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.teacher.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TeachersController.class)
class TeachersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService<TeacherViewDTO, TeacherCreateEditDTO> teacherService;

    @MockBean
    private CourseService<CourseDTO> courseService;

    @Test
    @WithMockUser
    public void testGetTeachersWithSearchQuery() throws Exception {
        List<TeacherViewDTO> teachers = List.of(getTeacherDto(1L), getTeacherDto(2L));
        when(teacherService.getTeachersByFirstName(anyString())).thenReturn(teachers);

        mockMvc.perform(get("/teachers").param("search_query", "John"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/t_list"))
            .andExpect(model().attribute("teachersView", teachers));
    }

    @Test
    @WithMockUser
    public void testGetTeachersWithoutSearchQuery() throws Exception {
        List<TeacherViewDTO> teachers = List.of(getTeacherDto(1L), getTeacherDto(2L));
        when(teacherService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/teachers"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/t_list"))
            .andExpect(model().attribute("teachersView", teachers));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testNewTeacher() throws Exception {
        List<CourseDTO> courses = List.of(new CourseDTO());
        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/teachers/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("teachers/t_new"))
            .andExpect(model().attributeExists("teacherView"))
            .andExpect(model().attributeExists("teacherCreate"))
            .andExpect(model().attribute("courses", courses));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateTeacherSuccess() throws Exception {
        TeacherCreateEditDTO teacherCreateEditDTO = TeacherCreateEditDTO.builder()
            .user(getUserDto(1L))
            .courseIds(List.of(1, 2))
            .build();

        when(courseService.getAllCourses()).thenReturn(List.of(new CourseDTO()));

        mockMvc.perform(post("/teachers")
                .flashAttr("teacherCreate", teacherCreateEditDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/teachers"));
    }

    private UserDTO getUserDto(long id) {
        return UserDTO.builder()
            .firstName("teacher" + id)
            .lastName("teacher" + id)
            .authorities(List.of(AuthorityDTO.builder().role(Role.TEACHER).build()))
            .build();
    }

    private TeacherViewDTO getTeacherDto(long id) {
        return TeacherViewDTO.builder()
            .user(getUserDto(id))
            .courses(List.of())
            .build();
    }

}