package dev.alexcoss.universitygenerator.mapper;

import dev.alexcoss.universitygenerator.dto.GGroup;
import dev.alexcoss.universitygenerator.model.Group;
import dev.alexcoss.universitygenerator.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(source = "students", target = "studentUsernames", qualifiedByName = "mapStudentsToUsernames")
    GGroup groupToGGroup(Group group);

    @Mapping(source = "studentUsernames", target = "students", ignore = true)
    Group gGroupToGroup(GGroup gGroup);

    @Named("mapStudentsToUsernames")
    default List<String> mapStudentsToUsernames(List<Student> students) {
        return students.stream()
            .map(Student::getUsername)
            .toList();
    }
}
