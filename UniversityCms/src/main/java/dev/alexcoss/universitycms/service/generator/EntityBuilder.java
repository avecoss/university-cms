package dev.alexcoss.universitycms.service.generator;

import dev.alexcoss.universitycms.dto.view.person.PersonDTO;
import dev.alexcoss.universitycms.model.Person;

public interface EntityBuilder<T extends Person, S extends PersonDTO> {
    T buildEntity(S dto);
}
