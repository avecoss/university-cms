package dev.alexcoss.universitycms.controller.group;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.dto.StudentDTO;
import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.GroupService;
import dev.alexcoss.universitycms.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupsController.class)
class GroupsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService<GroupDTO> groupService;

    @Test
    public void testGroups() throws Exception {
        List<StudentDTO> studentsForGroup = new ArrayList<>();
        studentsForGroup.add(new StudentDTO());

        List<GroupDTO> groups = new ArrayList<>();
        groups.add(new GroupDTO(1, "AA-123", studentsForGroup));
        groups.add(new GroupDTO(2, "BB-11", studentsForGroup));

        when(groupService.findAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_list"))
            .andExpect(model().attribute("groups", groups));
    }

    @Test
    public void testNewGroup() throws Exception {
        mockMvc.perform(get("/groups/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups/g_new"))
            .andExpect(model().attributeExists("group"));
    }

    @Test
    public void testCreateGroup() throws Exception {
        GroupDTO groupDTO = new GroupDTO(1, "BB-11", new ArrayList<>());

        mockMvc.perform(post("/groups")
                .param("name", "AA-123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/groups"));

        verify(groupService, times(1)).saveGroup(any(GroupDTO.class));
    }
}