package dev.alexcoss.universitygenerator.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "students")
@AllArgsConstructor
@Builder
public class GroupDTO {

    @Pattern(regexp = "^[A-Z]{2}-\\d{2,3}$")
    private String name;
    @JsonManagedReference
    @ToString.Exclude
    @Builder.Default
    private List<StudentDTO> students = new ArrayList<>();
}
