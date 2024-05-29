package dev.alexcoss.universitycms.dto.data;

import dev.alexcoss.universitycms.dto.data.response.GCourse;
import dev.alexcoss.universitycms.dto.data.response.GGroup;
import dev.alexcoss.universitycms.dto.data.response.GStudent;
import dev.alexcoss.universitycms.dto.data.response.GTeacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDataResponse {

    private List<GStudent> students;
    private List<GGroup> groups;
    private List<GCourse> courses;
    private List<GTeacher> teachers;
}
