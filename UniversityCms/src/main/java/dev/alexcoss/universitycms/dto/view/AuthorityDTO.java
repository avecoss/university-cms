package dev.alexcoss.universitycms.dto.view;

import dev.alexcoss.universitycms.enumerated.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityDTO {
    private Integer id;
    private Role role;

    @Override
    public String toString() {
        return role.toString();
    }
}
