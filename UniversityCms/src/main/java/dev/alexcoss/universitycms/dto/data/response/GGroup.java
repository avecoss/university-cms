package dev.alexcoss.universitycms.dto.data.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "studentUsernames")
@AllArgsConstructor
public class GGroup {

    private String name;
    @ToString.Exclude
    private List<String> studentUsernames = new ArrayList<>();
}
