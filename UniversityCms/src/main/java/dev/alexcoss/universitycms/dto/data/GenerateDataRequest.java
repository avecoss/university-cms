package dev.alexcoss.universitycms.dto.data;

import lombok.Data;

@Data
public class GenerateDataRequest {

    private int numberOfStudents = 1;
    private int numberOfGroups = 1 ;
    private int numberOfCourses = 1 ;
    private int numberOfTeachers = 1;
}
