package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.CourseDTO;
import dev.alexcoss.universitygenerator.dto.GroupDTO;
import dev.alexcoss.universitygenerator.dto.StudentDTO;
import dev.alexcoss.universitygenerator.enumerated.Role;
import dev.alexcoss.universitygenerator.service.generator.PersonGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StudentsManager {
    private final PersonGenerator<StudentDTO> personGenerator;
    private final Random random = new Random();

    public List<StudentDTO> getStudents(int amount, List<GroupDTO> groups, List<CourseDTO> courses) {
        List<StudentDTO> students = personGenerator.generatePersons(Role.STUDENT, amount);

        for (StudentDTO student : students) {
            GroupDTO randomGroup = getRandomGroup(groups);
            student.setGroup(randomGroup);
            randomGroup.getStudents().add(student);
        }

        for (CourseDTO course : courses) {
            int studentsCount = getRandomStudentsCount(amount);
            Set<StudentDTO> randomStudents = getRandomStudents(students, studentsCount, course);
            course.getStudents().addAll(randomStudents);
        }

        return students;
    }

    private GroupDTO getRandomGroup(List<GroupDTO> groups) {
        return groups.get(random.nextInt(groups.size()));
    }

    private int getRandomStudentsCount(int totalStudents) {
        return random.nextInt(totalStudents) + 1;
    }

    private Set<StudentDTO> getRandomStudents(List<StudentDTO> students, int count, CourseDTO course) {
        Set<StudentDTO> randomStudents = new HashSet<>();

        while (randomStudents.size() < count) {
            StudentDTO randomStudent = students.get(random.nextInt(students.size()));
            randomStudent.getCourses().add(course);
            randomStudents.add(randomStudent);
        }

        return randomStudents;
    }
}
