package dev.alexcoss.universitycms.dto.view.users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "password", callSuper = true)
@SuperBuilder
public class AccountDTO extends PersonDTO {

    @NotEmpty(message = "{person.validation.not_empty_username}")
    @Size(min = 2, max = 100, message = "{person.validation.size_username}")
    private String username;

    @NotEmpty(message = "{person.validation.not_empty_pass}")
    @Size(min = 8, max = 100, message = "{person.validation.size_pass}")
    private String password;
}
