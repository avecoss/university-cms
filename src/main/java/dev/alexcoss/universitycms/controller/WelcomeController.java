package dev.alexcoss.universitycms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping
    public String welcome(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }
}
