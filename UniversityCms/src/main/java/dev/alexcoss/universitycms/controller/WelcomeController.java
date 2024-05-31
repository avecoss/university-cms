package dev.alexcoss.universitycms.controller;

import dev.alexcoss.universitycms.security.PersonDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class WelcomeController {
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping
    public String welcome(Model model, @AuthenticationPrincipal PersonDetails user) {
        model.addAttribute("appName", appName);
        model.addAttribute("userDetails", user);

        return "index";
    }
}
