package dev.alexcoss.universitycms.dto.users;

import dev.alexcoss.universitycms.dto.CourseDTO;
import dev.alexcoss.universitycms.dto.GroupDTO;
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
