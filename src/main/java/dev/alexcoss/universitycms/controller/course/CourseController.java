package dev.alexcoss.universitycms.controller.course;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.TeacherService;
import dev.alexcoss.universitycms.service.TeacherServiceImpl;
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
@RequestMapping("/courses/{id}")
public class CourseController {

    private final CourseService<CourseDTO> courseService;
    private final TeacherService<TeacherDTO> teacherService;

    private final MessageSource messageSource;

    @GetMapping
    public String courseDetails(@PathVariable("id") int id, Model model, Locale locale) {
        if (courseService.findCourseById(id).isPresent()) {
            model.addAttribute("course", courseService.findCourseById(id).get());
        } else {
            throw new EntityNotExistException(messageSource.getMessage("course.errors.not_found",
                new Object[]{id}, "Course with ID {0} not found!", locale));
        }

        return "courses/c_details";
    }

    @GetMapping("/edit")
    public String editCourse(@PathVariable("id") int id, Model model, Locale locale) {
        if (courseService.findCourseById(id).isPresent()) {
            model.addAttribute("course", courseService.findCourseById(id).get());
            model.addAttribute("teachers", teacherService.findAllTeachers());
        } else {
            throw new EntityNotExistException(messageSource.getMessage("course.errors.not_found",
                new Object[]{id}, "Course with ID {0} not found!", locale));
        }
        return "courses/c_edit";
    }

    @PatchMapping
    public String updateCourse(@ModelAttribute("course") @Valid CourseDTO course, BindingResult bindingResult,
                               @PathVariable int id, @RequestParam Long teacherId, Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllTeachers());
            return "courses/c_edit";
        }

        if (teacherId != null) {
            course.setTeacher(teacherService.findTeacherById(teacherId)
                .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("teacher.errors.not_found",
                    new Object[]{teacherId}, "Teacher with ID {0} not found!", locale))));
        } else {
            throw new IllegalEntityException(messageSource.getMessage("teacher.errors.invalid_teacher_id",
                new Object[0], "Teacher ID is null", locale));
        }

        courseService.updateCourse(id, course);
        return "redirect:/courses";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") int id) {
        courseService.deleteCourseById(id);
        return "redirect:/courses";
    }
}
