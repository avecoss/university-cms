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
@RequestMapping("/teachers")
public class TeachersController {

    private final TeacherProcessingService<TeacherViewDTO, TeacherCreateEditDTO> teacherService;
    private final CourseProcessingService<CourseDTO> courseService;

    @GetMapping
    public String teachers(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("teachersView", teacherService.findTeachersByFirstName(searchQuery));
        } else {
            model.addAttribute("teachersView", teacherService.findAllTeachers());
        }
        return "teachers/t_list";
    }

    @GetMapping("/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacherView", new TeacherViewDTO());
        model.addAttribute("teacherCreate", new TeacherCreateEditDTO());
        model.addAttribute("courses", courseService.findAllCourses());
        return "teachers/t_new";
    }

    @PostMapping()
    public String createTeacher(@ModelAttribute("teacherCreate") @Valid TeacherCreateEditDTO teacher, BindingResult bindingResult,
                                @RequestParam(value = "courseIds", required = false) List<Integer> courseIds, Model model, Locale locale) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teacherView", new TeacherViewDTO());
            model.addAttribute("courses", courseService.findAllCourses());
            return "teachers/t_new";
        }
        teacher.setCourseIds(courseIds);

        teacherService.saveTeacher(teacher, locale);
        return "redirect:/teachers";
    }
}
