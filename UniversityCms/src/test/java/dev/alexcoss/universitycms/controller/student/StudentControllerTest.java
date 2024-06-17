package dev.alexcoss.universitycms.controller.student;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.group.GroupService;
import dev.alexcoss.universitycms.service.student.StudentService;
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
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService<StudentViewDTO, StudentEditCreateDTO> studentService;

    @MockBean
    private GroupService<GroupDTO> groupService;

    @MockBean
    private CourseService<CourseDTO> courseService;

    @Test
    @WithMockUser
    public void testStudentDetails() throws Exception {
        long studentId = 1L;
        Locale locale = Locale.ENGLISH;
        GroupDTO group = GroupDTO.builder().name("group").build();
        StudentViewDTO studentViewDTO = StudentViewDTO.builder().id(studentId).user(getUserDto()).group(group).courses(List.of()).build();

        when(studentService.getStudentById(studentId, locale)).thenReturn(studentViewDTO);

        mockMvc.perform(get("/students/{id}", studentId))
            .andExpect(status().isOk())
            .andExpect(view().name("students/s_details"))
            .andExpect(model().attribute("studentView", studentViewDTO));
    }

    @Test
    @WithMockUser
    public void testStudentDetailsNotFound() throws Exception {
        long studentId = 1L;
        Locale locale = Locale.ENGLISH;

        doThrow(EntityNotExistException.class).when(studentService).getStudentById(studentId, locale);

        mockMvc.perform(get("/students/{id}", studentId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testEditStudent() throws Exception {
        long studentId = 1L;
        Locale locale = Locale.ENGLISH;
        GroupDTO group = GroupDTO.builder().name("group").build();
        StudentViewDTO studentViewDTO = StudentViewDTO.builder().id(studentId).user(getUserDto()).group(group).courses(List.of()).build();
        List<GroupDTO> groupDTOs = List.of(group, GroupDTO.builder().name("group2").build());
        List<CourseDTO> courseDTOs = List.of();

        when(studentService.getStudentById(studentId, locale)).thenReturn(studentViewDTO);
        when(groupService.getAllGroups()).thenReturn(groupDTOs);
        when(courseService.getAllCourses()).thenReturn(courseDTOs);

        mockMvc.perform(get("/students/{id}/edit", studentId))
            .andExpect(status().isOk())
            .andExpect(view().name("students/s_edit"))
            .andExpect(model().attribute("studentView", studentViewDTO))
            .andExpect(model().attribute("groups", groupDTOs))
            .andExpect(model().attribute("courses", courseDTOs));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateStudent() throws Exception {
        long studentId = 1L;
        StudentEditCreateDTO studentEditCreateDTO = StudentEditCreateDTO.builder().id(studentId).user(getUserDto()).groupId(1).courseIds(List.of(1, 2)).build();

        mockMvc.perform(patch("/students/{id}", studentId)
                .flashAttr("student", studentEditCreateDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteStudent() throws Exception {
        long studentId = 1L;

        mockMvc.perform(delete("/students/{id}", studentId).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).deleteStudentById(studentId);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteNonExistingStudent() throws Exception {
        long studentId = 1L;

        doThrow(EntityNotExistException.class).when(studentService).deleteStudentById(studentId);

        mockMvc.perform(delete("/students/{id}", studentId).with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    private UserDTO getUserDto() {
        return UserDTO.builder()
            .firstName("student")
            .lastName("student")
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }
}