package dev.alexcoss.universitycms.dto.view.user;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDTO {
    private Long id;

    @NotEmpty(message = "{person.validation.not_empty_firstname}")
    @Size(min = 2, max = 50, message = "{person.validation.size_firstname}")
    private String firstName;

    @NotEmpty(message = "{person.validation.not_empty_lastname}")
    @Size(min = 2, max = 50, message = "{person.validation.size_lastname}")
    private String lastName;

    @Builder.Default
    private List<AuthorityDTO> authorities = List.of();
}
