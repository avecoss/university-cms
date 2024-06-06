package dev.alexcoss.universitycms.dto.view.student;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class StudentViewDTO extends StudentDTO{

    private GroupDTO group;
    @ToString.Exclude
    @Builder.Default
    private List<CourseDTO> courses = new ArrayList<>();
}
