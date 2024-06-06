package dev.alexcoss.universitycms.dto.view.student;

import dev.alexcoss.universitycms.dto.view.person.PersonDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class StudentDTO extends PersonDTO {
    private Long id;
}
