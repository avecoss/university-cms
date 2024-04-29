package dev.alexcoss.universitycms.controller.student;

import dev.alexcoss.universitycms.dto.GroupDTO;
import dev.alexcoss.universitycms.dto.StudentDTO;
import dev.alexcoss.universitycms.service.GroupService;
import dev.alexcoss.universitycms.service.StudentService;
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
@RequestMapping("/students/{id}")
public class StudentController {

    private final StudentService<StudentDTO> studentService;
    private final GroupService<GroupDTO> groupService;

    private final MessageSource messageSource;

    @GetMapping
    public String studentDetails(@PathVariable("id") long id, Model model, Locale locale) {
        if (studentService.findStudentById(id).isPresent()) {
            model.addAttribute("student", studentService.findStudentById(id).get());
        } else {
            throw new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{id}, "Student with ID {0} not found!", locale));
        }

        return "students/s_details";
    }

    @GetMapping("/edit")
    public String editStudent(@PathVariable("id") long id, Model model, Locale locale) {
        if (studentService.findStudentById(id).isPresent()) {
            model.addAttribute("student", studentService.findStudentById(id).get());
            model.addAttribute("groups", groupService.findAllGroups());
        } else {
            throw new EntityNotExistException(messageSource.getMessage("student.errors.not_found",
                new Object[]{id}, "Student with ID {0} not found!", locale));
        }
        return "students/s_edit";
    }

    @PatchMapping
    public String updateStudent(@ModelAttribute("student") @Valid StudentDTO student, BindingResult bindingResult,
                               @PathVariable long id, @RequestParam Integer groupId, Locale locale) {
        if (bindingResult.hasErrors())
            return "students/s_edit";

        if (groupId != null) {
            student.setGroup(groupService.findGroupById(groupId)
                .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                    new Object[]{groupId}, "Group with ID {0} not found!", locale))));
        } else {
            throw new IllegalEntityException(messageSource.getMessage("group.errors.invalid_group_id",
                new Object[0], "Group ID is null", locale));
        }

        studentService.updateStudent(id, student);
        return "redirect:/students";
    }

    @DeleteMapping
    public String deleteCourse(@PathVariable("id") long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }
}
