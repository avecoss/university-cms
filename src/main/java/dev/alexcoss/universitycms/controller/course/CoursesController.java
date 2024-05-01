package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.TeacherService;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.exception.IllegalEntityException;
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
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService<CourseDTO> courseService;
    private final TeacherService<TeacherDTO> teacherService;

    private final MessageSource messageSource;

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
                               @RequestParam Long teacherId, Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllTeachers());
            return "courses/c_new";
        }

        if (teacherId != null) {
            course.setTeacher(teacherService.findTeacherById(teacherId)
                .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                    new Object[]{teacherId}, "Teacher with ID {0} not found!", locale))));
        } else {
            throw new IllegalEntityException(messageSource.getMessage("teacher.errors.invalid_teacher_id",
                new Object[0], "Teacher ID is null", locale));
        }

        courseService.saveCourse(course);
        return "redirect:/courses";
    }
}
