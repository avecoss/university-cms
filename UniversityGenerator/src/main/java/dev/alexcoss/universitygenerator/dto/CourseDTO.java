package dev.alexcoss.universitygenerator.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {

    private String name;
    @JsonManagedReference
    private TeacherDTO teacher;
    @JsonManagedReference
    @ToString.Exclude
    @Builder.Default
    private Set<StudentDTO> students = new HashSet<>();
}
