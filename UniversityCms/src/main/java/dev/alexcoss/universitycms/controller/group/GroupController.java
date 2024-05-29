package dev.alexcoss.universitycms.controller.group;

import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.service.group.GroupProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/groups/{id}")
public class GroupController {

    private final GroupProcessingService<GroupDTO> groupService;

    @GetMapping
    public String groupDetails(@PathVariable("id") int id, Model model) {
        model.addAttribute("group", groupService.findGroupById(id));
        return "groups/g_details";
    }

    @GetMapping("/edit")
    public String editGroup(@PathVariable("id") int id, Model model) {
        model.addAttribute("group", groupService.findGroupById(id));
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
