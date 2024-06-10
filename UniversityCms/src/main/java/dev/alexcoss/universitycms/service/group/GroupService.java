package dev.alexcoss.universitycms.service.group;

import java.util.List;

public interface GroupService<T>{

    List<T> getAllGroups();
    List<T> getGroupsByLetters(String letter);
    T getGroupById(Integer id);
    void updateGroup(Integer id, T updated);
    void saveGroup(T Group);
    void deleteGroupById(Integer id);
}
