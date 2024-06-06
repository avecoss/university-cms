package dev.alexcoss.universitycms.dto.view.person;

import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class PersonDTO {
    private UserDTO user;
}
