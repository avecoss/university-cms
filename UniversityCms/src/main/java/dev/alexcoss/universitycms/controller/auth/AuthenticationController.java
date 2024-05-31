package dev.alexcoss.universitycms.controller.auth;

import dev.alexcoss.universitycms.dto.view.users.PersonAuthDTO;
import dev.alexcoss.universitycms.service.auth.RegistrationService;
import dev.alexcoss.universitycms.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") PersonAuthDTO person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("person") @Valid PersonAuthDTO person, BindingResult bindingResult) { //todo handle exception
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "auth/registration";

        registrationService.register(person);

        return "redirect:/login";
    }
}
