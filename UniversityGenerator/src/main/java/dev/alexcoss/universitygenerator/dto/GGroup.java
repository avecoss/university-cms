package dev.alexcoss.universitygenerator.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GGroup {

    @Pattern(regexp = "^[A-Z]{2}-\\d{2,3}$")
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<String> studentUsernames = new ArrayList<>();
}
