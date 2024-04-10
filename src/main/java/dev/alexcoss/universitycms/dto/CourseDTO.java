package dev.alexcoss.universitycms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {

    private int id;
    private String name;
    private TeacherDTO teacher;
}
