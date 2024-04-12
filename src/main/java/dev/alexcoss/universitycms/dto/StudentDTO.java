package dev.alexcoss.universitycms.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(of = "id", callSuper = true)
@SuperBuilder
public class StudentDTO extends PersonDTO {

    private long id;
    private GroupDTO group;
    private Set<CourseDTO> courses;
}
