package dev.alexcoss.universitygenerator.service.factory;

import dev.alexcoss.universitygenerator.dto.PersonDTO;

public interface PersonFactory<T extends PersonDTO> {
    T createPerson(String firstName, String lastName, String username, String password);
}
