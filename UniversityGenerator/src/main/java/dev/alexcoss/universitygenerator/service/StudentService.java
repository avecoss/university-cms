package dev.alexcoss.universitygenerator.service;

import dev.alexcoss.universitygenerator.dto.StudentDTO;
import dev.alexcoss.universitygenerator.model.Student;
import dev.alexcoss.universitygenerator.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository repository;
    private final ModelMapper modelMapper;

    public List<StudentDTO> findAllStudents() {
        Iterable<Student> students = repository.findAll();

        return StreamSupport.stream(students.spliterator(), false)
            .map(student -> modelMapper.map(student, StudentDTO.class))
            .toList();
    }
}
