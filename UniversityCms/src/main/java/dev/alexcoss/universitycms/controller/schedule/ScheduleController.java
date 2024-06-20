package dev.alexcoss.universitycms.controller.schedule;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.schedule.ScheduleDTO;
import dev.alexcoss.universitycms.dto.view.schedule.ScheduleEditCreateDTO;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.group.GroupService;
import dev.alexcoss.universitycms.service.schedule.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final CourseService<CourseDTO> courseService;
    private final GroupService<GroupDTO> groupService;

    @GetMapping
    public String allSchedules(Model model) {
        model.addAttribute("schedules", scheduleService.getAllSortedByStartTime());
        model.addAttribute("groups", groupService.getAllGroups());
        return "schedules/sch_list";
    }

    @GetMapping("/{id}")
    public String scheduleDetails(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getScheduleById(id));
        return "schedules/sch_details";
    }

    @GetMapping("/new")
    public String newScheduleForm(Model model) {
        model.addAttribute("schedule", new ScheduleDTO());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("groups", groupService.getAllGroups());
        return "schedules/sch_new";
    }

    @PostMapping
    public String createSchedule(@ModelAttribute @Valid ScheduleEditCreateDTO scheduleEditCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("schedule", new ScheduleDTO());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("groups", groupService.getAllGroups());
            return "schedules/sch_new";
        }

        scheduleService.saveSchedule(scheduleEditCreateDTO);
        return "redirect:/schedules";
    }

    @GetMapping("/{id}/edit")
    public String editScheduleForm(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getScheduleById(id));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("groups", groupService.getAllGroups());
        return "schedules/sch_edit";
    }

    @PatchMapping("/{id}")
    public String updateSchedule(@ModelAttribute @Valid ScheduleEditCreateDTO scheduleEditCreateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("groups", groupService.getAllGroups());
        }
        scheduleService.updateSchedule(scheduleEditCreateDTO);
        return "redirect:/schedules";
    }

    @DeleteMapping("/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/schedules";
    }

    @GetMapping("/group")
    public String allSchedulesByGroupId(@RequestParam("groupId") Integer groupId, Model model) {
        model.addAttribute("schedules", scheduleService.getAllByGroupId(groupId));
        model.addAttribute("groups", groupService.getAllGroups());
        return "schedules/sch_list";
    }
}
