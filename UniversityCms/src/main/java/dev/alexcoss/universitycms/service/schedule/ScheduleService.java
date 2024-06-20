package dev.alexcoss.universitycms.service.schedule;

import dev.alexcoss.universitycms.dto.view.schedule.ScheduleDTO;
import dev.alexcoss.universitycms.dto.view.schedule.ScheduleEditCreateDTO;

import java.util.List;

public interface ScheduleService {

    List<ScheduleDTO> getAllSortedByStartTime();

    ScheduleDTO getScheduleById(Long id);

    void saveSchedule(ScheduleEditCreateDTO scheduleDTO);

    void updateSchedule(ScheduleEditCreateDTO scheduleDTO);

    void deleteSchedule(Long id);
}
