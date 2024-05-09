package dev.alexcoss.universitycms.controller.student;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.dto.users.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.users.StudentViewDTO;
import dev.alexcoss.universitycms.service.CourseService;
import dev.alexcoss.universitycms.service.GroupService;
import dev.alexcoss.universitycms.service.StudentService;
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
@RequestMapping("/students/{id}")
public class StudentController {

    private final StudentService<StudentViewDTO, StudentEditCreateDTO> studentService;
    private final GroupService<GroupDTO> groupService;
    private final CourseService<CourseDTO> courseService;

    @GetMapping
    public String studentDetails(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("studentView", studentService.findStudentById(id, locale));
        return "students/s_details";
    }

    @GetMapping("/edit")
    public String editStudent(@PathVariable("id") long id, Model model, Locale locale) {
        model.addAttribute("studentView", studentService.findStudentById(id, locale));
        model.addAttribute("studentEdite", new StudentEditCreateDTO());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("courses", courseService.findAllCourses());
        return "students/s_edit";
    }

    @PatchMapping
    public String updateStudent(@ModelAttribute("student") @Valid StudentEditCreateDTO student, BindingResult bindingResult,
                                @PathVariable long id, @RequestParam Integer groupId,
                                @RequestParam(value = "courseIds", required = false) List<Integer> courseIds,
                                Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAllGroups());
            return "students/s_edit";
        }

        studentService.updateStudent(id, student, locale);
        return "redirect:/students";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}
