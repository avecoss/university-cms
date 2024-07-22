package dev.alexcoss.universitygenerator.service;

import dev.alexcoss.universitygenerator.dto.GGroup;
import dev.alexcoss.universitygenerator.mapper.GroupMapper;
import dev.alexcoss.universitygenerator.model.Group;
import dev.alexcoss.universitygenerator.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository repository;
    private final GroupMapper groupMapper;

    public List<GGroup> findAllGroups() {
        Iterable<Group> groups = repository.findAll();

        return StreamSupport.stream(groups.spliterator(), false)
            .map(groupMapper::groupToGGroup)
            .toList();
    }
}
