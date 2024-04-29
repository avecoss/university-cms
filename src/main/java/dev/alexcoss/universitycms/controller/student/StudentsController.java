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
@RequestMapping("/students")
public class StudentsController {

    private final StudentService<StudentDTO> studentService;
    private final GroupService<GroupDTO> groupService;

    private final MessageSource messageSource;

    @GetMapping
    public String students(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("students", studentService.findStudentsByFirstName(searchQuery));
        } else {
            model.addAttribute("students", studentService.findAllStudents());
        }
        return "students/s_list";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("groups", groupService.findAllGroups());
        return "students/s_new";
    }

    @PostMapping()
    public String createStudent(@ModelAttribute("student") @Valid StudentDTO student, BindingResult bindingResult,
                               @RequestParam Integer groupId, Locale locale) {
        if (bindingResult.hasErrors())
            return "students/s_new";

        if (groupId != null) {
            student.setGroup(groupService.findGroupById(groupId)
                .orElseThrow(() -> new EntityNotExistException(messageSource.getMessage("group.errors.not_found",
                    new Object[]{groupId}, "Group with ID {0} not found!", locale))));
        } else {
            throw new IllegalEntityException(messageSource.getMessage("group.errors.invalid_group_id",
                new Object[0], "Group ID is null", locale));
        }

        studentService.saveStudent(student);
        return "redirect:/students";
    }
}
