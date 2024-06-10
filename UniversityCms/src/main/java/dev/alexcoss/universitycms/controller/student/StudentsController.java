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

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/students")
public class StudentsController {

    private final StudentService<StudentViewDTO, StudentEditCreateDTO> studentService;
    private final GroupService<GroupDTO> groupService;
    private final CourseService<CourseDTO> courseService;

    @GetMapping
    public String students(Model model, @RequestParam(value = "search_query", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("studentsView", studentService.getStudentsByFirstName(searchQuery));
        } else {
            model.addAttribute("studentsView", studentService.getAllStudents());
        }
        return "students/s_list";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        model.addAttribute("studentView", new StudentViewDTO());
        model.addAttribute("studentCreate", new StudentEditCreateDTO());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("courses", courseService.getAllCourses());
        return "students/s_new";
    }

    @PostMapping()
    public String createStudent(@ModelAttribute("studentCreate") @Valid StudentEditCreateDTO student, BindingResult bindingResult,
                                @RequestParam Integer groupId,
                                @RequestParam (value = "courseIds", required = false) List<Integer> courseIds,
                                Locale locale, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("studentView", new StudentViewDTO());
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("courses", courseService.getAllCourses());
            return "students/s_new";
        }

        studentService.saveStudent(student, locale);
        return "redirect:/students";
    }
}
