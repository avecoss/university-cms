package dev.alexcoss.universitygenerator.service.generator;

import org.springframework.stereotype.Component;
import org.apache.commons.lang3.RandomStringUtils;

@Component
public class LoginPasswordGenerator {

    public String generateStartingLogin(String firstName, String lastName) {
        return loginGenerator(firstName, lastName);
    }

    public String generateStartingPassword() {
        return "password";
    }

    private String loginGenerator(String firstName, String lastName) {
        String baseLogin =  firstName.substring(0, Math.min(3, firstName.length())) +
            lastName.substring(0, Math.min(3, lastName.length())).toLowerCase();

        String randomSuffix = RandomStringUtils.randomAlphanumeric(6);
        return baseLogin + randomSuffix;
    }
}
