package dev.alexcoss.universitygenerator.service;

import dev.alexcoss.universitygenerator.dto.GroupDTO;
import dev.alexcoss.universitygenerator.model.Group;
import dev.alexcoss.universitygenerator.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository repository;
    private final ModelMapper modelMapper;

    public List<GroupDTO> findAllGroups() {
        Iterable<Group> groups = repository.findAll();

        return StreamSupport.stream(groups.spliterator(), false)
            .map(group -> modelMapper.map(group, GroupDTO.class))
            .toList();
    }
}
