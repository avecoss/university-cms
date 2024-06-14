package dev.alexcoss.universitycms.controller.group;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.group.GroupService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupsController.class)
class GroupsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService<GroupDTO> groupService;

    @Test
    @WithMockUser
    public void testGroups() throws Exception {
        List<StudentViewDTO> students = List.of(getStudentDto(1L), getStudentDto(2L));
        List<GroupDTO> groups = List.of(
            GroupDTO.builder().id(1).name("AA-123").students(students).build(),
            GroupDTO.builder().id(2).name("BB-123").students(students).build());

        when(groupService.getAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_list"))
            .andExpect(model().attribute("groups", groups));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testNewGroup() throws Exception {
        mockMvc.perform(get("/groups/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_new"))
            .andExpect(model().attributeExists("group"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateGroup() throws Exception {
        List<StudentViewDTO> students = List.of(getStudentDto(1L), getStudentDto(2L));
        GroupDTO groupDTO = GroupDTO.builder().id(1).name("AA-123").students(students).build();

        mockMvc.perform(post("/groups")
                .flashAttr("group", groupDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).saveGroup(any(GroupDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateGroupValidationFailure() throws Exception {
        GroupDTO groupDTO = GroupDTO.builder().id(1).name("").students(new ArrayList<>()).build();

        mockMvc.perform(post("/groups")
                .flashAttr("group", groupDTO)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_new"))
            .andExpect(model().attributeExists("group"))
            .andExpect(model().hasErrors());

        verify(groupService, times(0)).saveGroup(any(GroupDTO.class));
    }

    private StudentViewDTO getStudentDto(@NotNull Long id) {
        return StudentViewDTO.builder()
            .id(id)
            .user(getUserDto())
            .build();
    }

    private UserDTO getUserDto() {
        return UserDTO.builder()
            .firstName("student")
            .lastName("student")
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }
}