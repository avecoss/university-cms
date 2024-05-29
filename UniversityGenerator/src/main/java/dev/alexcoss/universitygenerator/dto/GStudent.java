package dev.alexcoss.universitygenerator.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class StudentDTO extends PersonDTO {

    @JsonBackReference
    private GroupDTO group;
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<CourseDTO> courses = new HashSet<>();
}
