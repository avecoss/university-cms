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
@RequestMapping("/students")
public class StudentsController {

    private final StudentProcessingService<StudentViewDTO, StudentEditCreateDTO> studentService;
    private final GroupProcessingService<GroupDTO> groupService;
    private final CourseProcessingService<CourseDTO> courseService;

    @GetMapping
    public String students(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("studentsView", studentService.findStudentsByFirstName(searchQuery));
        } else {
            model.addAttribute("studentsView", studentService.findAllStudents());
        }
        return "students/s_list";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        model.addAttribute("studentView", new StudentViewDTO());
        model.addAttribute("studentCreate", new StudentEditCreateDTO());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("courses", courseService.findAllCourses());
        return "students/s_new";
    }

    @PostMapping()
    public String createStudent(@ModelAttribute("studentCreate") @Valid StudentEditCreateDTO student, BindingResult bindingResult,
                                @RequestParam Integer groupId,
                                @RequestParam (value = "courseIds", required = false) List<Integer> courseIds,
                                Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAllGroups());
            return "students/s_new";
        }

        studentService.saveStudent(student, locale);
        return "redirect:/students";
    }
}
