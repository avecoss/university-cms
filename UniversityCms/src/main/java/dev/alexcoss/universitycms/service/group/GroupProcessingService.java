package dev.alexcoss.universitycms.service.group;

import java.util.List;

public interface GroupProcessingService<T> extends GroupService{

    List<T> findAllGroups();
    List<T> findGroupsByLetters(String letter);
    T findGroupById(Integer id);
    void updateGroup(Integer id, T updated);
    void saveGroup(T Group);
    void deleteGroupById(Integer id);
}
