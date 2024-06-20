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
@WebMvcTest(StudentsController.class)
class StudentsControllerTest {

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
    public void testGetStudentsWithSearchQuery() throws Exception {
        List<StudentViewDTO> students = List.of(getStudentDto(1L), getStudentDto(2L));
        when(studentService.getStudentsByFirstName(anyString())).thenReturn(students);

        mockMvc.perform(get("/students").param("search_query", "John"))
            .andExpect(status().isOk())
            .andExpect(view().name("students/s_list"))
            .andExpect(model().attribute("studentsView", students));
    }

    @Test
    @WithMockUser
    public void testGetStudentsWithoutSearchQuery() throws Exception {
        List<StudentViewDTO> students = List.of(getStudentDto(1L), getStudentDto(2L));
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
            .andExpect(status().isOk())
            .andExpect(view().name("students/s_list"))
            .andExpect(model().attribute("studentsView", students));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testNewStudent() throws Exception {
        List<GroupDTO> groups = List.of(new GroupDTO());
        List<CourseDTO> courses = List.of(new CourseDTO());
        when(groupService.getAllGroups()).thenReturn(groups);
        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/students/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("students/s_new"))
            .andExpect(model().attributeExists("studentView"))
            .andExpect(model().attributeExists("studentCreate"))
            .andExpect(model().attribute("groups", groups))
            .andExpect(model().attribute("courses", courses));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateStudentSuccess() throws Exception {
        StudentEditCreateDTO studentEditCreateDTO = StudentEditCreateDTO.builder()
            .user(getUserDto(1L))
            .groupId(1)
            .courseIds(List.of(1, 2))
            .build();

        when(groupService.getAllGroups()).thenReturn(List.of(new GroupDTO()));
        when(courseService.getAllCourses()).thenReturn(List.of(new CourseDTO()));

        mockMvc.perform(post("/students")
                .flashAttr("studentCreate", studentEditCreateDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/students"));
    }

    private UserDTO getUserDto(long id) {
        return UserDTO.builder()
            .firstName("student" + id)
            .lastName("student" + id)
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }

    private StudentViewDTO getStudentDto(long id) {
        return StudentViewDTO.builder()
            .user(getUserDto(id))
            .group(new GroupDTO())
            .courses(List.of())
            .build();
    }
}