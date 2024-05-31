package dev.alexcoss.universitycms.service.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LoginPasswordGenerator {

    public String generateStartingLogin(String firstName, String lastName, Set<String> existingLogins) {
        int index = 1;
        String login = loginGenerator(firstName, lastName, index);

        while (existingLogins.contains(login)) {
            login = loginGenerator(firstName, lastName, index);
            index++;
        }
        return login;
    }

    public String generateStartingPassword() {
        return "password";
    }

    private String loginGenerator(String firstName, String lastName, int index) {
        String baseLogin =  firstName.substring(0, Math.min(3, firstName.length())) +
            lastName.substring(0, Math.min(3, lastName.length())).toLowerCase();

        String randomSuffix = RandomStringUtils.randomAlphanumeric(6);
        return baseLogin + randomSuffix;
    }
}
