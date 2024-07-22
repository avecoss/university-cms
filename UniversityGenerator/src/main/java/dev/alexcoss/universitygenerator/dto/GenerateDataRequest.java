package dev.alexcoss.universitygenerator.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GenerateDataRequest {
    @Min(value = 1, message = "Number of students must be at least 1")
    private int numberOfStudents;

    @Min(value = 1, message = "Number of groups must be at least 1")
    private int numberOfGroups;

    @Min(value = 1, message = "Number of courses must be at least 1")
    private int numberOfCourses;

    @Min(value = 1, message = "Number of teachers must be at least 1")
    private int numberOfTeachers;
}
