package dev.alexcoss.universitycms.dto.users;

import dev.alexcoss.universitycms.dto.CourseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "courses")
@SuperBuilder
public class TeacherViewDTO extends TeacherDTO{
    @Builder.Default
    private List<CourseDTO> courses = new ArrayList<>();
}
