package dev.alexcoss.universitycms.controller.student;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.student.StudentViewDTO;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.group.GroupService;
import dev.alexcoss.universitycms.service.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/students/{id}")
public class StudentController {

    private final StudentService<StudentViewDTO, StudentEditCreateDTO> studentService;
    private final GroupService<GroupDTO> groupService;
    private final CourseService<CourseDTO> courseService;

    @GetMapping
    public String studentDetails(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("studentView", studentService.getStudentById(id, locale));
        return "students/s_details";
    }

    @GetMapping("/edit")
    public String editStudent(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("studentView", studentService.getStudentById(id, locale));
        model.addAttribute("studentEdite", new StudentEditCreateDTO());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("courses", courseService.getAllCourses());
        return "students/s_edit";
    }

    @PatchMapping
    public String updateStudent(@ModelAttribute("student") @Valid StudentEditCreateDTO student, BindingResult bindingResult,
                                Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.getAllGroups());
            return "students/s_edit";
        }

        studentService.updateStudent(student, locale);
        return "redirect:/students";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}
