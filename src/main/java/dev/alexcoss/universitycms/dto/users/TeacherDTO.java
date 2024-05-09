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
@ToString(callSuper = true)
@SuperBuilder
public class TeacherDTO extends PersonDTO {

    @Builder.Default
    @ToString.Exclude
    private List<CourseDTO> courses = new ArrayList<>();
    @Builder.Default
    @ToString.Exclude
    private List<Integer> courseIds = new ArrayList<>();
}
