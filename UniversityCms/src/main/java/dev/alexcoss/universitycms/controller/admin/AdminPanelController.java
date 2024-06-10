package dev.alexcoss.universitycms.controller.admin;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminPanelController {

    private final UserService userService;

    @GetMapping
    public String adminPanel(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "users_per_page", required = false, defaultValue = "10") Integer usersPerPage,
                             @RequestParam(value = "sort_by_username", required = false, defaultValue = "false") boolean sortByUsername) {

        model.addAttribute("currentPage", page);
        model.addAttribute("usersPerPage", usersPerPage);
        model.addAttribute("sortByUsername", sortByUsername);

        Page<UserDTO> userPage = userService.getWithPagination(page, usersPerPage, sortByUsername);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());

        return "admin/admin";
    }

    @GetMapping("/users/{id}/role")
    public String adminPanelRole(Model model, @PathVariable long id) {
        UserDTO user = userService.getUserById(id);
        List<Role> userRoles = user.getAuthorities().stream()
            .map(AuthorityDTO::getRole)
            .toList();

        model.addAttribute("user", user);
        model.addAttribute("userRoles", userRoles);

        return "admin/user_role";
    }

    @PatchMapping("/users/{id}")
    public String adminPanelRoleUpdate(@PathVariable long id, @RequestParam(value = "roles", required = false) List<Role> roles) {

        userService.updateUserAuthority(id, roles);
        return "redirect:/admin";
    }
}
