package dev.alexcoss.universitycms.dto.view.teacher;

import dev.alexcoss.universitycms.dto.view.person.PersonDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class TeacherDTO extends PersonDTO {
    private Long id;
}
