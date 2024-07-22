package dev.alexcoss.universitygenerator.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GCourse {

    private String name;
    private String teacherUsername;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<String> studentUsernames = new HashSet<>();
}
