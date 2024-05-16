package dev.alexcoss.universitygenerator.service;

import dev.alexcoss.universitygenerator.dto.TeacherDTO;
import dev.alexcoss.universitygenerator.model.Teacher;
import dev.alexcoss.universitygenerator.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository repository;
    private final ModelMapper modelMapper;

    public List<TeacherDTO> findAllGTeachers() {
        Iterable<Teacher> teachers = repository.findAll();

        return StreamSupport.stream(teachers.spliterator(), false)
            .map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
            .toList();
    }
}
