package dev.alexcoss.universitygenerator.service.factory;

import dev.alexcoss.universitygenerator.dto.GPerson;

public interface PersonFactory<T extends GPerson> {
    T createPerson(String firstName, String lastName, String username, String password);
}
