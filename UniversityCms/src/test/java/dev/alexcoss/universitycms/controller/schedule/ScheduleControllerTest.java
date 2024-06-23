package dev.alexcoss.universitycms.controller.schedule;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.CourseDTO;
import dev.alexcoss.universitycms.dto.view.GroupDTO;
import dev.alexcoss.universitycms.dto.view.schedule.ScheduleDTO;
import dev.alexcoss.universitycms.dto.view.schedule.ScheduleEditCreateDTO;
import dev.alexcoss.universitycms.dto.view.teacher.TeacherViewDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.course.CourseService;
import dev.alexcoss.universitycms.service.group.GroupService;
import dev.alexcoss.universitycms.service.schedule.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private GroupService<GroupDTO> groupService;

    @MockBean
    private CourseService<CourseDTO> courseService;

    @Test
    @WithMockUser
    public void testGetAllSchedules() throws Exception {
        List<ScheduleDTO> scheduleDTOS = List.of(getScheduleDTO(1L), getScheduleDTO(2L));
        when(scheduleService.getAllSortedByStartTime()).thenReturn(scheduleDTOS);

        mockMvc.perform(get("/schedules"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedules/sch_list"))
            .andExpect(model().attribute("schedules", scheduleDTOS));
    }

    @Test
    @WithMockUser
    public void testScheduleDetails() throws Exception {
        long scheduleId = 1L;
        ScheduleDTO scheduleDTO = getScheduleDTO(scheduleId);

        when(scheduleService.getScheduleById(scheduleId)).thenReturn(scheduleDTO);

        mockMvc.perform(get("/schedules/{id}", scheduleId))
            .andExpect(status().isOk())
            .andExpect(view().name("schedules/sch_details"))
            .andExpect(model().attribute("schedule", scheduleDTO));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testNewScheduleForm() throws Exception {
        List<GroupDTO> groups = List.of(new GroupDTO());
        List<CourseDTO> courses = List.of(new CourseDTO());
        when(groupService.getAllGroups()).thenReturn(groups);
        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/schedules/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedules/sch_new"))
            .andExpect(model().attributeExists("schedule"))
            .andExpect(model().attribute("groups", groups))
            .andExpect(model().attribute("courses", courses));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateSchedule() throws Exception {
        long scheduleId = 1L;
        ScheduleEditCreateDTO scheduleEditCreateDTO = ScheduleEditCreateDTO.builder()
            .id(scheduleId)
            .courseId(1)
            .groupId(1)
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.MAX)
            .build();

        mockMvc.perform(patch("/schedules/{id}", scheduleId)
                .flashAttr("schedule", scheduleEditCreateDTO)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/schedules"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteSchedule() throws Exception {
        long scheduleId = 1L;

        mockMvc.perform(delete("/schedules/{id}", scheduleId).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/schedules"));

        verify(scheduleService, times(1)).deleteSchedule(scheduleId);
    }


    @Test
    @WithMockUser
    public void testGetAllSchedulesByGroupId() throws Exception {
        Integer groupId = 1;
        List<ScheduleDTO> scheduleDTOS = List.of(getScheduleDTO(1L), getScheduleDTO(2L));
        GroupDTO group1 = GroupDTO.builder().id(1).name("group").build();
        GroupDTO group2 = GroupDTO.builder().id(2).name("group").build();

        when(scheduleService.getAllByGroupId(groupId)).thenReturn(scheduleDTOS);
        when(groupService.getAllGroups()).thenReturn(List.of(group1, group2));

        mockMvc.perform(get("/schedules/group").param("groupId",groupId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("schedules/sch_list"))
            .andExpect(model().attributeExists("schedules"))
            .andExpect(model().attributeExists("groups"));

        verify(scheduleService, times(1)).getAllByGroupId(groupId);
        verify(groupService, times(1)).getAllGroups();
    }

    private ScheduleDTO getScheduleDTO(long id) {
        return ScheduleDTO.builder()
            .id(id)
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.MAX)
            .course(CourseDTO.builder().id(1).name("course").teacher(TeacherViewDTO.builder().id(1L).user(getUserDto()).build()).build())
            .group(GroupDTO.builder().name("group").id(1).build())
            .build();
    }

    private UserDTO getUserDto() {
        return UserDTO.builder()
            .firstName("teacher")
            .lastName("teacher")
            .authorities(List.of(AuthorityDTO.builder().role(Role.TEACHER).build()))
            .build();
    }
}