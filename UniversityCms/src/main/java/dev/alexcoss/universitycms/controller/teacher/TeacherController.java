package dev.alexcoss.universitycms.controller.teacher;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.users.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.users.TeacherViewDTO;
import dev.alexcoss.universitycms.service.course.CourseProcessingService;
import dev.alexcoss.universitycms.service.teacher.TeacherProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/teachers/{id}")
public class TeacherController {

    private final TeacherProcessingService<TeacherViewDTO, TeacherCreateEditDTO> teacherService;
    private final CourseProcessingService<CourseDTO> courseService;

    @GetMapping
    public String teacherDetails(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("teacherView", teacherService.findTeacherById(id, locale));
        return "teachers/t_details";
    }

    @GetMapping("/edit")
    public String editTeacher(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("teacherView", teacherService.findTeacherById(id, locale));
        model.addAttribute("teacherEdit", new TeacherCreateEditDTO());
        model.addAttribute("courses", courseService.findAllCourses());

        return "teachers/t_edit";
    }

    @PatchMapping
    public String updateTeacher(@ModelAttribute("teacherEdit") @Valid TeacherCreateEditDTO teacher, BindingResult bindingResult,
                                @PathVariable long id, @RequestParam(value = "courseIds", required = false) List<Integer> courseIds,
                                Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAllCourses());
            return "teachers/t_edit";
        }
        teacher.setCourseIds(courseIds);

        teacherService.updateTeacher(id, teacher, locale);
        return "redirect:/teachers";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") long id) {
        teacherService.deleteTeacherById(id);
        return "redirect:/teachers";
    }
}
