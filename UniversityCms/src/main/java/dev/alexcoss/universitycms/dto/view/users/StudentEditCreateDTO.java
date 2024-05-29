package dev.alexcoss.universitycms.dto.view.users;

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
public class StudentEditCreateDTO extends StudentDTO {

    @ToString.Exclude
    private Integer groupId;
    @ToString.Exclude
    @Builder.Default
    private List<Integer> courseIds = new ArrayList<>();

}
