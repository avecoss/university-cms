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
@RequestMapping("/groups")
public class GroupsController {

    private final GroupProcessingService<GroupDTO> groupService;

    @GetMapping
    public String groups(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("groups", groupService.findGroupsByLetters(searchQuery));
        } else {
            model.addAttribute("groups", groupService.findAllGroups());
        }
        return "groups/g_list";
    }

    @GetMapping("/new")
    public String newGroup(Model model) {
        model.addAttribute("group", new GroupDTO());
        return "groups/g_new";
    }

    @PostMapping()
    public String createGroup(@ModelAttribute("group") @Valid GroupDTO group, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "groups/g_new";

        groupService.saveGroup(group);
        return "redirect:/groups";
    }
}
