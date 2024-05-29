package dev.alexcoss.universitycms.service.group;

import dev.alexcoss.universitycms.dto.data.response.GGroup;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.repository.GroupRepository;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateGroupServiceImpl implements GeneratedGroupService<GGroup> {

    private final GroupRepository repository;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void saveGroups(List<GGroup> gGroups, Map<String, Student> studentMap) {
        isValidGroupList(gGroups);

        List<Group> groupList = new ArrayList<>();
        for (GGroup gGroup : gGroups) {
            Group group = new Group();
            group.setName(gGroup.getName());

            for (String studentUsername : gGroup.getStudentUsernames()) {
                group.addStudent(studentMap.get(studentUsername));
            }
            groupList.add(group);
        }

        repository.saveAllAndFlush(groupList);
    }

    private void isValidGroupList(List<GGroup> groupList) {
        if (groupList == null || groupList.isEmpty()) {
            throw new NullEntityListException(messageSource.getMessage("group.errors.empty_list", new Object[0],
                "Group list is null or empty", LocaleContextHolder.getLocale()));
        }

        for (GGroup group : groupList) {
            isValidGroup(group);
        }
    }

    private void isValidGroup(GGroup group) {
        if (group == null || group.getName() == null || group.getName().isEmpty()) {
            throw new IllegalEntityException(messageSource.getMessage("group.errors.invalid", new Object[0],
                "Invalid group data", LocaleContextHolder.getLocale()));
        }
    }
}
