package dev.alexcoss.universitycms.controller.group;

import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
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
    public void testGroupDetails() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(new ArrayList<>()).build();

        when(groupService.findGroupById(groupId)).thenReturn(groupDTO);

        mockMvc.perform(get("/groups/{id}", groupId))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_details"))
            .andExpect(model().attribute("group", groupDTO));
    }

    @Test
    public void testGroupDetailsNotFound() throws Exception {
        int groupId = 1;

        doThrow(EntityNotExistException.class).when(groupService).findGroupById(1);

        mockMvc.perform(get("/groups/{id}", groupId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }

    @Test
    public void testUpdateGroupValidationFailure() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(new ArrayList<>()).build();

        groupDTO.setName("");

        mockMvc.perform(patch("/groups/{id}", groupId)
                .flashAttr("group", groupDTO))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_edit"))
            .andExpect(model().attribute("group", groupDTO))
            .andExpect(model().hasErrors());
    }

    @Test
    public void testEditGroup() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(new ArrayList<>()).build();

        when(groupService.findGroupById(groupId)).thenReturn(groupDTO);

        mockMvc.perform(get("/groups/{id}/edit", groupId))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_edit"))
            .andExpect(model().attribute("group", groupDTO));
    }

    @Test
    public void testUpdateGroup() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = GroupDTO.builder().id(groupId).name("AA-123").students(new ArrayList<>()).build();

        mockMvc.perform(patch("/groups/{id}", groupId)
                .flashAttr("group", groupDTO))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups"));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        int groupId = 1;

        mockMvc.perform(delete("/groups/{id}", groupId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).deleteGroupById(groupId);
    }

    @Test
    public void testDeleteNonExistingGroup() throws Exception {
        int groupId = 1;

        doThrow(EntityNotExistException.class).when(groupService).deleteGroupById(groupId);

        mockMvc.perform(delete("/groups/{id}", groupId))
            .andExpect(status().isNotFound())
            .andExpect(view().name("errors/404"));
    }
}