package dev.alexcoss.universitycms.service;

import java.util.List;
import java.util.Optional;

public interface GroupService<T> {

    List<T> findAllGroups();
    List<T> findGroupsByLetters(String letter);
    Optional<T> findGroupById(Integer id);
    void saveGroups(List<T> list);
    void updateGroup(Integer id, T updated);
    void saveGroup(T Group);
    void deleteGroupById(Integer id);
}
