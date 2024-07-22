package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.GGroup;
import dev.alexcoss.universitygenerator.service.generator.GroupGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupsManager {

    private final GroupGenerator groupGenerator;

    public List<GGroup> getGroups(int amount) {
        return groupGenerator.generateGroupList(amount);
    }
}
