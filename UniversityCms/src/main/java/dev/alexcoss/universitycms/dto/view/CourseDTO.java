package dev.alexcoss.universitycms.dto.view;

import dev.alexcoss.universitycms.dto.view.teacher.TeacherDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "teacher")
@Builder
public class CourseDTO {

    private Integer id;

    @NotEmpty(message = "{course.validation.not_empty}")
    @Size(min = 2, max = 100, message = "{course.validation.size}")
    private String name;

    private TeacherDTO teacher;
}
