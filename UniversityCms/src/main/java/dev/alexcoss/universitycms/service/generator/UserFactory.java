package dev.alexcoss.universitycms.service.generator;

import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final LoginPasswordGenerator loginPasswordGenerator;

    public User createUser(UserDTO userDTO, Role role) {
        String firstName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();
        String username = loginPasswordGenerator.generateStartingLogin(firstName, lastName);

        return User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(loginPasswordGenerator.generateStartingPassword())
            .email(username + "@universitycms.com")
            .authorities(Set.of(Authority.builder().role(role).build()))
            .build();
    }
}
