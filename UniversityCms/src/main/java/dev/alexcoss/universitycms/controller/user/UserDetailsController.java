package dev.alexcoss.universitycms.controller.user;

import dev.alexcoss.universitycms.dto.view.user.UserEditDTO;
import dev.alexcoss.universitycms.security.PersonDetails;
import dev.alexcoss.universitycms.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String updateUser(@ModelAttribute("user") @Valid UserEditDTO user, BindingResult bindingResult,
                             @RequestParam("current_password") String password,
                             @AuthenticationPrincipal PersonDetails currentUser,
                             HttpServletRequest request) throws ServletException {

        user.setUsername(currentUser.getPerson().getUsername());
        user.setAuthorities(currentUser.getPerson().getAuthorities());
        user.setEmail(currentUser.getPerson().getEmail());

        if (!userService.checkPassword(password, user))
            bindingResult.rejectValue("password", "password.invalid", "Password does not match");

        if (bindingResult.hasErrors())
            return "user/edit";

        userService.updateUser(user);

        request.logout();
        return "redirect:/login?logout";
    }
}
