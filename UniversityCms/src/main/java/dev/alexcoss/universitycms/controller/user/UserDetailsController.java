package dev.alexcoss.universitycms.controller.user;

import dev.alexcoss.universitycms.dto.view.users.PersonEditDTO;
import dev.alexcoss.universitycms.security.PersonDetails;
import dev.alexcoss.universitycms.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/details")
public class UserDetailsController {

    private final UserService userService;

    @GetMapping
    public String userDetails(@AuthenticationPrincipal PersonDetails user, Model model) {
        model.addAttribute("user", user);

        return "user/details";
    }

    @GetMapping("/edit")
    public String editUser(@AuthenticationPrincipal PersonDetails user, Model model) {
        model.addAttribute("user", user.getPerson());

        return "user/edit";
    }

    @PatchMapping
    public String updateUser(@ModelAttribute("user") @Valid PersonEditDTO user, BindingResult bindingResult,
                             @RequestParam("current_password") String password,
                             @AuthenticationPrincipal PersonDetails currentUser) {

        user.setUsername(currentUser.getPerson().getUsername());
        user.setRole(currentUser.getPerson().getRole());

        if (!userService.checkPassword(password, user))
            bindingResult.rejectValue("password", "password.invalid", "Password does not match");

        if (bindingResult.hasErrors())
            return "user/edit";

        userService.updateUser(user);
        return "redirect:/logout";
    }
}
