package dev.alexcoss.universitycms.service.group;

import dev.alexcoss.universitycms.model.Student;

import java.util.List;
import java.util.Map;

public interface GeneratedGroupService<T> extends GroupService<T> {

    void saveGroups(List<T> list, Map<String, Student> map);
}
