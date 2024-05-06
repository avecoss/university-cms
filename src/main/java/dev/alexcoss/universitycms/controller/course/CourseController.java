package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.users.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.users.TeacherViewDTO;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@RequiredArgsConstructor
@Controller
@RequestMapping("/courses/{id}")
public class CourseController {

    private final CourseService<CourseDTO> courseService;
    private final TeacherService<TeacherViewDTO, TeacherCreateEditDTO> teacherService;

    @GetMapping
    public String courseDetails(@PathVariable("id") int id, Model model, Locale locale) {
        model.addAttribute("course", courseService.findCourseById(id, locale));
        return "courses/c_details";
    }

    @GetMapping("/edit")
    public String editCourse(@PathVariable("id") int id, Model model, Locale locale) {
        model.addAttribute("course", courseService.findCourseById(id, locale));
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "courses/c_edit";
    }

    @PatchMapping
    public String updateCourse(@ModelAttribute("course") @Valid CourseDTO course, BindingResult bindingResult,
                               @PathVariable int id, @RequestParam Long teacherId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllTeachers());
            return "courses/c_edit";
        }

        course.setTeacher(teacherService.findTeacherById(teacherId));

        courseService.updateCourse(id, course);
        return "redirect:/courses";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") int id) {
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }
}
