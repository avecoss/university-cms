package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherCreateEditDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.teacher.TeacherService;
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
        model.addAttribute("course", courseService.getCourseById(id, locale));
        return "courses/c_details";
    }

    @GetMapping("/edit")
    public String editCourse(@PathVariable("id") int id, Model model, Locale locale) {
        model.addAttribute("course", courseService.getCourseById(id, locale));
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "courses/c_edit";
    }

    @PatchMapping
    public String updateCourse(@ModelAttribute("course") @Valid CourseDTO course, BindingResult bindingResult,
                               @RequestParam Long teacherId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            return "courses/c_edit";
        }

        course.setTeacher(teacherService.getTeacherById(teacherId));

        courseService.updateCourse(course);
        return "redirect:/courses";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") int id) {
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }
}
