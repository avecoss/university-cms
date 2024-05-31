package dev.alexcoss.universitygenerator.dto;

import dev.alexcoss.universitygenerator.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class GPerson {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;
}
