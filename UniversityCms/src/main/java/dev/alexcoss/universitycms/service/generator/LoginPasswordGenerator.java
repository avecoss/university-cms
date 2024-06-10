package dev.alexcoss.universitycms.service.generator;

import dev.alexcoss.universitycms.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LoginPasswordGenerator {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public String generateStartingLogin(String firstName, String lastName) {
        String login = loginGenerator(firstName, lastName);
        Set<String> existingLogins = userService.getAllUsernames();

        while (existingLogins.contains(login)) {
            login = loginGenerator(firstName, lastName);
        }
        return login;
    }

    public String generateStartingPassword() {
        return passwordEncoder.encode("password");
    }

    private String loginGenerator(String firstName, String lastName) {
        String baseLogin =  firstName.substring(0, Math.min(3, firstName.length())) +
            lastName.substring(0, Math.min(3, lastName.length())).toLowerCase();

        String randomSuffix = RandomStringUtils.randomAlphanumeric(6);
        return baseLogin + randomSuffix;
    }
}
