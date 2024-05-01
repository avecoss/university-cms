package dev.alexcoss.universitycms.controller.teacher;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.TeacherService;
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

    private final TeacherService<TeacherDTO> teacherService;
    private final CourseService<CourseDTO> courseService;

    @GetMapping
    public String teacherDetails(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("teacher", teacherService.findTeacherById(id, locale));
        return "teachers/t_details";
    }

    @GetMapping("/edit")
    public String editTeacher(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("teacher", teacherService.findTeacherById(id, locale));
        model.addAttribute("courses", courseService.findAllCourses());

        return "teachers/t_edit";
    }

    @PatchMapping
    public String updateTeacher(@ModelAttribute("teacher") @Valid TeacherDTO teacher, BindingResult bindingResult,
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
