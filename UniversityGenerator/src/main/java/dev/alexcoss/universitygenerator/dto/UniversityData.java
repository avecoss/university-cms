package dev.alexcoss.universitygenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityData {

    private List<StudentDTO> students;
    private List<GroupDTO> groups;
    private List<CourseDTO> courses;
    private List<TeacherDTO> teachers;
}
