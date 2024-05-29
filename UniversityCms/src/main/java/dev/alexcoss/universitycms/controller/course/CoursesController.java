package dev.alexcoss.universitycms.controller.course;

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

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CourseProcessingService<CourseDTO> courseService;
    private final TeacherProcessingService<TeacherViewDTO, TeacherCreateEditDTO> teacherService;

    @GetMapping
    public String courses(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("courses", courseService.findCoursesByLetters(searchQuery));
        } else {
            model.addAttribute("courses", courseService.findAllCourses());
        }
        return "courses/c_list";
    }

    @GetMapping("/new")
    public String newCourse(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "courses/c_new";
    }

    @PostMapping()
    public String createCourse(@ModelAttribute("course") @Valid CourseDTO course, BindingResult bindingResult,
                               @RequestParam Long teacherId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllTeachers());
            return "courses/c_new";
        }

        course.setTeacher(teacherService.findTeacherById(teacherId));

        courseService.saveCourse(course);
        return "redirect:/courses";
    }
}
