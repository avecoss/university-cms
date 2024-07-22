package dev.alexcoss.universitygenerator.service.manager;

import dev.alexcoss.universitygenerator.dto.GCourse;
import dev.alexcoss.universitygenerator.dto.GGroup;
import dev.alexcoss.universitygenerator.dto.GStudent;
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
    private final PersonGenerator<GStudent> personGenerator;
    private final Random random = new Random();

    public List<GStudent> getStudents(int amount, List<GGroup> groups, List<GCourse> courses) {
        List<GStudent> students = personGenerator.generatePersons(Role.STUDENT, amount);

        for (GStudent student : students) {
            GGroup randomGroup = getRandomGroup(groups);
            randomGroup.getStudentUsernames().add(student.getUsername());
        }

        for (GCourse course : courses) {
            int studentsCount = getRandomStudentsCount(amount);
            Set<String> randomStudents = getRandomStudents(students, studentsCount);
            course.getStudentUsernames().addAll(randomStudents);
        }

        return students;
    }

    private GGroup getRandomGroup(List<GGroup> groups) {
        return groups.get(random.nextInt(groups.size()));
    }

    private int getRandomStudentsCount(int totalStudents) {
        return random.nextInt(totalStudents) + 1;
    }

    private Set<String> getRandomStudents(List<GStudent> students, int count) {
        Set<String> randomStudents = new HashSet<>();

        while (randomStudents.size() < count) {
            GStudent randomStudent = students.get(random.nextInt(students.size()));
            randomStudents.add(randomStudent.getUsername());
        }

        return randomStudents;
    }
}
