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

    private List<GStudent> students;
    private List<GGroup> groups;
    private List<GCourse> courses;
    private List<GTeacher> teachers;
}
