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
@RequestMapping("/teachers")
public class TeachersController {

    private final TeacherService<TeacherDTO> teacherService;
    private final CourseService<CourseDTO> courseService;

    @GetMapping
    public String teachers(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("teachers", teacherService.findTeachersByFirstName(searchQuery));
        } else {
            model.addAttribute("teachers", teacherService.findAllTeachers());
        }
        return "teachers/t_list";
    }

    @GetMapping("/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        model.addAttribute("courses", courseService.findAllCourses());
        return "teachers/t_new";
    }

    @PostMapping()
    public String createTeacher(@ModelAttribute("teacher") @Valid TeacherDTO teacher, BindingResult bindingResult,
                                @RequestParam(value = "courseIds", required = false) List<Integer> courseIds, Model model, Locale locale) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAllCourses());
            return "teachers/t_new";
        }
        teacher.setCourseIds(courseIds);

        teacherService.saveTeacher(teacher, locale);
        return "redirect:/teachers";
    }
}
