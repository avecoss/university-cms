package dev.alexcoss.universitycms.service.group;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.repository.GroupRepository;

import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Locale;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {
    @MockBean
    private GroupRepository repository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private GroupServiceImpl groupService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGroupWithValidGroup() {
        GroupDTO updatedGroupDTO = new GroupDTO();
        updatedGroupDTO.setName("Updated Group");

        Group existingGroup = new Group();
        existingGroup.setName("Existing Group");

        when(repository.findById(anyInt())).thenReturn(Optional.of(existingGroup));
        when(modelMapper.map(any(GroupDTO.class), eq(Group.class))).thenReturn(existingGroup);
        when(repository.save(any(Group.class))).thenReturn(existingGroup);

        groupService.updateGroup(1, updatedGroupDTO);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingGroup);
        assertEquals("Updated Group", existingGroup.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveGroupWithValidGroup() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("New Group");

        Group group = new Group();
        group.setName("New Group");

        when(modelMapper.map(any(GroupDTO.class), eq(Group.class))).thenReturn(group);
        when(repository.save(any(Group.class))).thenReturn(group);

        groupService.saveGroup(groupDTO);

        verify(repository, times(1)).save(group);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroupById() {
        Group group = new Group();
        group.setName("Existing Group");

        when(repository.findById(anyInt())).thenReturn(Optional.of(group));

        groupService.deleteGroupById(1);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGroupByIdThrowsEntityNotExistException() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class)))
            .thenReturn("Group with ID 1 not found!");

        EntityNotExistException exception = assertThrows(EntityNotExistException.class, () -> groupService.deleteGroupById(1));

        assertEquals("Group with ID 1 not found!", exception.getMessage());
        verify(repository, times(1)).findById(1);
        verify(repository, times(0)).deleteById(anyInt());
    }
}