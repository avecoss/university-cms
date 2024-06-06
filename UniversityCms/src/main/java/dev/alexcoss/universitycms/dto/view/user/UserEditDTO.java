package dev.alexcoss.universitycms.dto.view.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class UserEditDTO extends UserDTO {

    private String username;

    @NotEmpty(message = "{person.validation.not_empty_pass}")
    @Size(min = 6, max = 100, message = "{person.validation.size_pass}")
    private String password;

    private String email;
}
