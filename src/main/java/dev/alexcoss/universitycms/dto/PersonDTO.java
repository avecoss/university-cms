package dev.alexcoss.universitycms.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "username")
@SuperBuilder
public abstract class PersonDTO {

    @NotEmpty(message = "First name should not be empty")
    @Size(min = 2, max = 100, message = "First name should be between 2 and 100 characters")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @Size(min = 2, max = 100, message = "Last name should be between 2 and 100 characters")
    private String lastName;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 250, message = "Password should be between 8 and 250 characters")
    private String password;
}
