package dev.alexcoss.universitycms.dto.data.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GCourse {

    private String name;

    private String teacherUsername;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<String> studentUsernames = new HashSet<>();
}
