package dev.alexcoss.universitycms.service;

import dev.alexcoss.universitycms.dto.TeacherDTO;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.service.exception.EntityNotExistException;
import dev.alexcoss.universitycms.service.exception.IllegalEntityException;
import dev.alexcoss.universitycms.service.exception.NullEntityListException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository repository;
    private final ModelMapper modelMapper;

    public Optional<TeacherDTO> getTeacherById(long id) {
        return repository.findById(id)
            .map(teacher -> modelMapper.map(teacher, TeacherDTO.class));

    }

    public List<TeacherDTO> getTeachers() {
        List<Teacher> teachers = repository.findAll();
        return teachers.stream()
            .map(student -> modelMapper.map(student, TeacherDTO.class))
            .toList();
    }

    @Transactional
    public void addTeachers(List<TeacherDTO> teacherList) {
        isValidTeachersList(teacherList);

        List<Teacher> teachers = teacherList.stream()
            .map(teacherDTO -> modelMapper.map(teacherDTO, Teacher.class))
            .toList();

        repository.saveAllAndFlush(teachers);
    }

    @Transactional
    public void addTeacher(TeacherDTO teacher) {
        isValidTeacher(teacher);
        repository.save(modelMapper.map(teacher, Teacher.class));
    }

    @Transactional
    public void removeTeacherById(long teacherId) {
        Optional<TeacherDTO> existingTeacher = getTeacherById(teacherId);

        if (existingTeacher.isPresent()) {
            repository.deleteById(teacherId);
        } else {
            throw new EntityNotExistException("Teacher with ID " + teacherId + " not found");
        }
    }

    private void isValidTeacher(TeacherDTO teacher) {
        if (teacher != null && teacher.getFirstName() != null && teacher.getLastName() != null &&
            teacher.getUsername() != null && teacher.getPassword() != null) {
            throw new IllegalEntityException("Invalid teacher data");
        }
    }

    private void isValidTeachersList(List<TeacherDTO> teacherList) {
        if (teacherList == null || teacherList.isEmpty()) {
            throw new NullEntityListException("Teacher list is null or empty");
        }

        for (TeacherDTO teacher : teacherList) {
            isValidTeacher(teacher);
        }
    }
}
