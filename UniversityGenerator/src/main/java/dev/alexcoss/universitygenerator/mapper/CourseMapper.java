package dev.alexcoss.universitygenerator.mapper;

import dev.alexcoss.universitygenerator.dto.GCourse;
import dev.alexcoss.universitygenerator.model.Course;
import dev.alexcoss.universitygenerator.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "teacher.username", target = "teacherUsername")
    @Mapping(source = "students", target = "studentUsernames", qualifiedByName = "mapStudentsToUsernames")
    GCourse courseToGCourse(Course course);

    @Mapping(source = "teacherUsername", target = "teacher", ignore = true)
    @Mapping(source = "studentUsernames", target = "students", ignore = true)
    Course gCourseToCourse(GCourse gCourse);

    @Named("mapStudentsToUsernames")
    default Set<String> mapStudentsToUsernames(Set<Student> students) {
        return students.stream()
            .map(Student::getUsername)
            .collect(Collectors.toSet());
    }

    List<GCourse> coursesToGCourses(List<Course> courses);
}
