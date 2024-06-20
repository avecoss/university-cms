package dev.alexcoss.universitycms.controller.group;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.group.GroupService;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService<GroupDTO> groupService;

    @Test
    @WithMockUser
    public void testGroupDetails() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(List.of(getStudentDto(1L), getStudentDto(2L))).build();

        when(groupService.getGroupById(groupId)).thenReturn(groupDTO);

        mockMvc.perform(get("/groups/{id}", groupId))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_details"))
            .andExpect(model().attribute("group", groupDTO));
    }

    @Test
    @WithMockUser
    public void testGroupDetailsNotFound() throws Exception {
        int groupId = 1;

        doThrow(EntityNotExistException.class).when(groupService).getGroupById(groupId);

        mockMvc.perform(get("/groups/{id}", groupId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateGroupValidationFailure() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(List.of(getStudentDto(1L), getStudentDto(2L))).build();
        groupDTO.setName("");

        mockMvc.perform(patch("/groups/{id}", groupId)
                .flashAttr("group", groupDTO)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_edit"))
            .andExpect(model().attribute("group", groupDTO))
            .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testEditGroup() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(List.of(getStudentDto(1L), getStudentDto(2L))).build();

        when(groupService.getGroupById(groupId)).thenReturn(groupDTO);

        mockMvc.perform(get("/groups/{id}/edit", groupId))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_edit"))
            .andExpect(model().attribute("group", groupDTO));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateGroup() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(List.of(getStudentDto(1L), getStudentDto(2L))).build();

        mockMvc.perform(patch("/groups/{id}", groupId)
                .flashAttr("group", groupDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteGroup() throws Exception {
        int groupId = 1;

        mockMvc.perform(delete("/groups/{id}", groupId).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).deleteGroupById(groupId);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteNonExistingGroup() throws Exception {
        int groupId = 1;

        doThrow(EntityNotExistException.class).when(groupService).deleteGroupById(groupId);

        mockMvc.perform(delete("/groups/{id}", groupId).with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    private StudentViewDTO getStudentDto(@NotNull Long id) {
        return StudentViewDTO.builder()
            .id(id)
            .user(getUserDto())
            .build();
    }

    private UserDTO getUserDto(){
        return UserDTO.builder()
            .firstName("student")
            .lastName("student")
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }
}