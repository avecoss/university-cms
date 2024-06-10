package dev.alexcoss.universitycms.dto.view;

import dev.alexcoss.universitycms.dto.view.student.StudentViewDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDTO {

    private int id;

    @NotEmpty(message = "{course.validation.not_empty}")
    @Pattern(regexp = "^[A-Z]{2}-\\d{2,3}$", message = "{course.validation.pattern}")
    private String name;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<StudentViewDTO> students = new ArrayList<>();
}
