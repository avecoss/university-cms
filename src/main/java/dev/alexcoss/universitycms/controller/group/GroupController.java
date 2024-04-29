package dev.alexcoss.universitycms.controller.group;

import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.service.GroupService;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/groups/{id}")
public class GroupController {

    private final GroupService<GroupDTO> groupService;

    private final MessageSource messageSource;

    @GetMapping
    public String groupDetails(@PathVariable("id") int id, Model model, Locale locale) {
        if (groupService.findGroupById(id).isPresent()) {
            model.addAttribute("group", groupService.findGroupById(id).get());
        } else {
            throw new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", locale));
        }

        return "groups/g_details";
    }

    @GetMapping("/edit")
    public String editGroup(@PathVariable("id") int id, Model model, Locale locale) {
        if (groupService.findGroupById(id).isPresent()) {
            model.addAttribute("group", groupService.findGroupById(id).get());
        } else {
            throw new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                new Object[]{id}, "Group with ID {0} not found!", locale));
        }
        return "groups/g_edit";
    }

    @PatchMapping
    public String updateGroup(@ModelAttribute("group") @Valid GroupDTO group, BindingResult bindingResult,
                               @PathVariable int id) {
        if (bindingResult.hasErrors())
            return "groups/g_edit";

        groupService.updateGroup(id, group);
        return "redirect:/groups";
    }

    @DeleteMapping
    public String deleteGroup(@PathVariable("id") int id) {
        groupService.deleteGroupById(id);
        return "redirect:/groups";
    }
}
