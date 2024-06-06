package dev.alexcoss.universitycms.util;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserAuthDTO person = (UserAuthDTO) target;
        boolean userExists = userService.isUserByUsername(person.getUsername());

        if (userExists) {
            errors.rejectValue("username", "Username already exists", "Username already exists");
        }
    }
}
