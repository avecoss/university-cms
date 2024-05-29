package dev.alexcoss.universitycms.dto.data.response;

import dev.alexcoss.universitycms.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class GPerson {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;
}
