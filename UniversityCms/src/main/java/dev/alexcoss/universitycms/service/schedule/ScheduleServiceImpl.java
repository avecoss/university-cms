package dev.alexcoss.universitycms.service.schedule;

import dev.alexcoss.universitycms.dto.view.schedule.ScheduleDTO;
import dev.alexcoss.universitycms.dto.view.schedule.ScheduleEditCreateDTO;
import dev.alexcoss.universitycms.model.Schedule;
import dev.alexcoss.universitycms.repository.CourseRepository;
import dev.alexcoss.universitycms.repository.GroupRepository;
import dev.alexcoss.universitycms.repository.ScheduleRepository;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ScheduleDTO> getAllSortedByStartTime() {
        return scheduleRepository.findAll(Sort.by("startTime")).stream()
            .map(this::convertToDTO)
            .toList();
    }

    @Override
    public ScheduleDTO getScheduleById(Long id) {
        return convertToDTO(scheduleRepository.findById(id).orElseThrow(() -> new EntityNotExistException("Schedule not found")));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void saveSchedule(ScheduleEditCreateDTO scheduleDTO) {
        Schedule schedule = convertToEntity(scheduleDTO);
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUFF')")
    public void updateSchedule(ScheduleEditCreateDTO scheduleDTO) {
        Schedule schedule = convertToEntity(scheduleDTO);
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSchedule(Long id) {
        scheduleRepository.findById(id).orElseThrow(() -> new EntityNotExistException("Schedule not found"));
        scheduleRepository.deleteById(id);
    }

    private ScheduleDTO convertToDTO(Schedule schedule) {
        return modelMapper.map(schedule, ScheduleDTO.class);
    }

    private Schedule convertToEntity(ScheduleEditCreateDTO scheduleDTO) {
        isValidSchedule(scheduleDTO);
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

        schedule.setCourse(courseRepository.findById(scheduleDTO.getCourseId())
            .orElseThrow(() -> new EntityNotExistException("Course not found")));
        schedule.setGroup(groupRepository.findById(scheduleDTO.getGroupId())
            .orElseThrow(() -> new EntityNotExistException("Group not found")));

        return schedule;
    }

    private void isValidSchedule(ScheduleEditCreateDTO scheduleDTO) {
        if (scheduleDTO == null || scheduleDTO.getCourseId() == null || scheduleDTO.getGroupId() == null) {
            throw new IllegalEntityException("Invalid schedule data");
        }
    }
}
