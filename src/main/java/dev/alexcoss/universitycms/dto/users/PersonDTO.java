package dev.alexcoss.universitycms.dto.users;

import dev.alexcoss.universitycms.enumerated.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class PersonDTO {

    private long id;

    @NotEmpty(message = "{person.validation.not_empty_firstname}")
    @Size(min = 2, max = 50, message = "{person.validation.size_firstname}")
    private String firstName;

    @NotEmpty(message = "{person.validation.not_empty_lastname}")
    @Size(min = 2, max = 50, message = "{person.validation.size_lastname}")
    private String lastName;

    private Role role;
}
