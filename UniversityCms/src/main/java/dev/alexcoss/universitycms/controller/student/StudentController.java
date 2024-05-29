package dev.alexcoss.universitycms.controller.student;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.users.StudentEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.users.StudentViewDTO;
import dev.alexcoss.universitycms.service.course.CourseProcessingService;
import dev.alexcoss.universitycms.service.group.GroupProcessingService;
import dev.alexcoss.universitycms.service.student.StudentProcessingService;
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

    private final StudentProcessingService<StudentViewDTO, StudentEditCreateDTO> studentService;
    private final GroupProcessingService<GroupDTO> groupService;
    private final CourseProcessingService<CourseDTO> courseService;

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
