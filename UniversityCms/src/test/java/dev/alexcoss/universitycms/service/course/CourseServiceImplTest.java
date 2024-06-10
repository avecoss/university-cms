package dev.alexcoss.universitycms.service.course;

import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.repository.CourseRepository;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {
    @MockBean
    private CourseRepository repository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private CourseServiceImpl courseService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCourseWithValidCourse() {
        CourseDTO updatedCourseDTO = new CourseDTO();
        updatedCourseDTO.setName("Updated Course");

        Course existingCourse = new Course();
        existingCourse.setName("Existing Course");

        when(repository.findById(anyInt())).thenReturn(Optional.of(existingCourse));
        when(modelMapper.map(any(CourseDTO.class), eq(Course.class))).thenReturn(existingCourse);
        when(repository.save(any(Course.class))).thenReturn(existingCourse);

        courseService.updateCourse(1, updatedCourseDTO);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingCourse);
        assertEquals("Updated Course", existingCourse.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveCourseWithValidCourse() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("New Course");

        Course course = new Course();
        course.setName("New Course");

        when(modelMapper.map(any(CourseDTO.class), eq(Course.class))).thenReturn(course);
        when(repository.save(any(Course.class))).thenReturn(course);

        courseService.saveCourse(courseDTO);

        verify(repository, times(1)).save(course);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCourseById() {
        Course course = new Course();
        course.setName("Existing Course");

        when(repository.findById(anyInt())).thenReturn(Optional.of(course));

        courseService.deleteCourseById(1);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCourseByIdThrowsEntityNotExistException() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(Object[].class), anyString(), any(Locale.class)))
            .thenReturn("Course with ID 1 not found!");

        EntityNotExistException exception = assertThrows(EntityNotExistException.class, () ->
            courseService.deleteCourseById(1));

        assertEquals("Course with ID 1 not found!", exception.getMessage());
        verify(repository, times(1)).findById(1);
        verify(repository, times(0)).deleteById(anyInt());
    }
}