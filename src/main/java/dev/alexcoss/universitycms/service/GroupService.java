package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository repository;
    private final ModelMapper modelMapper;

    public List<GroupDTO> getGroups() {
        List<Group> groups = repository.findAll();

        return groups.stream()
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .toList();
    }


    @Transactional
    public void addGroups(List<GroupDTO> groupList) {
        if (isValidGroupList(groupList)) {
            List<Group> groups = groupList.stream()
                .map(groupDTO -> modelMapper.map(groupDTO, Group.class))
                .toList();

            repository.saveAllAndFlush(groups);
        }
    }

    @Transactional
    public void addGroup(GroupDTO groupDTO) {
        if (isValidGroup(groupDTO)) {
            repository.save(modelMapper.map(groupDTO, Group.class));
        }
    }

    private boolean isValidGroupList(List<GroupDTO> groupList) {
        if (groupList == null || groupList.isEmpty()) {
            log.error("Group list is null or empty");
            return false;
        }

        for (GroupDTO group : groupList) {
            if (!isValidGroup(group))
                return false;
        }
        return true;
    }

    private boolean isValidGroup(GroupDTO group) {
        if (group == null) {
            log.error("Invalid group in the list");
            return false;
        }
        return true;
    }
}
