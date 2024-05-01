package dev.alexcoss.universitycms.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(of = "id", callSuper = true)
@SuperBuilder
public class TeacherDTO extends PersonDTO {
    private long id;
    private List<Integer> courseIds;
}
