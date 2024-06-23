package dev.alexcoss.universitycms.service.schedule;

import dev.alexcoss.universitycms.dto.view.schedule.ScheduleEditCreateDTO;
import dev.alexcoss.universitycms.model.Course;
import dev.alexcoss.universitycms.model.Group;
import dev.alexcoss.universitycms.model.Schedule;
import dev.alexcoss.universitycms.repository.CourseRepository;
import dev.alexcoss.universitycms.repository.GroupRepository;
import dev.alexcoss.universitycms.repository.ScheduleRepository;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private ScheduleEditCreateDTO scheduleEditCreateDTO;
    private Schedule schedule;
    private Course course;
    private Group group;

    @BeforeEach
    void setUp() {
        scheduleEditCreateDTO = new ScheduleEditCreateDTO();
        scheduleEditCreateDTO.setCourseId(1);
        scheduleEditCreateDTO.setGroupId(1);
        schedule = new Schedule();
        course = new Course();
        group = new Group();
    }

    @Test
    @WithMockUser("hasRole('ADMIN')")
    void testSaveSchedule() {
        when(modelMapper.map(scheduleEditCreateDTO, Schedule.class)).thenReturn(schedule);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        scheduleService.saveSchedule(scheduleEditCreateDTO);

        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateSchedule() {
        when(modelMapper.map(scheduleEditCreateDTO, Schedule.class)).thenReturn(schedule);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        scheduleService.updateSchedule(scheduleEditCreateDTO);

        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteSchedule() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        scheduleService.deleteSchedule(1L);

        verify(scheduleRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteScheduleNotFound() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> scheduleService.deleteSchedule(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveScheduleInvalidData() {
        scheduleEditCreateDTO.setCourseId(null);

        assertThrows(IllegalEntityException.class, () -> scheduleService.saveSchedule(scheduleEditCreateDTO));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateScheduleInvalidData() {
        scheduleEditCreateDTO.setGroupId(null);

        assertThrows(IllegalEntityException.class, () -> scheduleService.updateSchedule(scheduleEditCreateDTO));
    }
}