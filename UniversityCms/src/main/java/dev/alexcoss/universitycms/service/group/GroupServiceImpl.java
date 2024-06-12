package dev.alexcoss.universitycms.service.group;

import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<GroupDTO> getAllGroups() {
        List<Group> groups = repository.findAll();

        return groups.stream()
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .toList();
    }

    @Override
    public List<GroupDTO> getGroupsByLetters(String letter) {
        List<Group> allByNameStartingWith = repository.findAllByNameStartingWith(letter);

        return allByNameStartingWith.stream()
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .toList();
    }

    @Override
    public GroupDTO getGroupById(Integer id) {
        return repository.findById(id)
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", LocaleContextHolder.getLocale())));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void saveGroup(GroupDTO groupDTO) {
        isValidGroup(groupDTO);
        repository.save(modelMapper.map(groupDTO, Group.class));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUFF')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGroupById(Integer id) {
        repository.findById(id)
            .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", LocaleContextHolder.getLocale())));

        repository.deleteById(id);
    }

    private void isValidGroup(GroupDTO group) {
        if (group == null || group.getName() == null || group.getName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("group.errors.invalid", new Object[0],
                "Invalid group data", LocaleContextHolder.getLocale()));
        }
    }
}
