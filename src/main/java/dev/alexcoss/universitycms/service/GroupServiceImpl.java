package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService<GroupDTO> {

    private final GroupRepository repository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Override
    public List<GroupDTO> findAllGroups() {
        List<Group> groups = repository.findAll();

        return groups.stream()
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .toList();
    }

    @Override
    public List<GroupDTO> findGroupsByLetters(String letter) {
        List<Group> allByNameStartingWith = repository.findAllByNameStartingWith(letter);

        return allByNameStartingWith.stream()
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .toList();
    }

    @Override
    public GroupDTO findGroupById(Integer id) {
        return repository.findById(id)
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", LocaleContextHolder.getLocale())));
    }

    @Transactional
    @Override
    public void saveGroups(List<GroupDTO> groupList) {
        isValidGroupList(groupList);

        List<Group> groups = groupList.stream()
            .map(groupDTO -> modelMapper.map(groupDTO, Group.class))
            .toList();

        repository.saveAllAndFlush(groups);
    }

    @Transactional
    @Override
    public void saveGroup(GroupDTO groupDTO) {
        isValidGroup(groupDTO);
        repository.save(modelMapper.map(groupDTO, Group.class));
    }

    @Transactional
    @Override
    public void updateGroup(Integer id, GroupDTO updated) {
        isValidGroup(updated);

        repository.findById(id)
            .map(group -> {
                group.setName(updated.getName());
                return repository.save(group);
            })
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", LocaleContextHolder.getLocale())));
    }

    @Transactional
    @Override
    public void deleteGroupById(Integer id) {
        repository.findById(id)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(id);
    }

    private void isValidGroupList(List<GroupDTO> groupList) {
        if (groupList == null || groupList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("group.errors.empty_list", new Object[0],
                "Group list is null or empty", LocaleContextHolder.getLocale()));
        }

        for (GroupDTO group : groupList) {
            isValidGroup(group);
        }
    }

    private void isValidGroup(GroupDTO group) {
        if (group == null || group.getName() == null || group.getName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("group.errors.invalid", new Object[0],
                "Invalid group data", LocaleContextHolder.getLocale()));
        }
    }
}
