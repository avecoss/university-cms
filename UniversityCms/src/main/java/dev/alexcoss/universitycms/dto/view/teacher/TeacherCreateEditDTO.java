package dev.alexcoss.universitycms.dto.view.teacher;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "courseIds")
@SuperBuilder
public class TeacherCreateEditDTO extends TeacherDTO {
    @Builder.Default
    private List<Integer> courseIds = new ArrayList<>();
}
