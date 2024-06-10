package dev.alexcoss.universitycms.controller.admin;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPanelController.class)
class AdminPanelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldIsOkStatusAdminPanel() throws Exception {
        List<UserDTO> userDTOList = Arrays.asList(
            new UserDTO(),
            new UserDTO()
        );
        Page<UserDTO> userPage = new PageImpl<>(userDTOList);

        given(userService.getWithPagination(anyInt(), anyInt(), anyBoolean())).willReturn(userPage);

        mockMvc.perform(get("/admin"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/admin"))
            .andExpect(model().attributeExists("users", "currentPage", "usersPerPage", "sortByUsername"))
            .andExpect(model().attribute("users", hasSize(userDTOList.size())))
            .andExpect(model().attribute("totalPages", userPage.getTotalPages()));
    }

    @Test
    public void shouldReturnClientErrorStatusForNonAdminUser() throws Exception {
        List<UserDTO> userDTOList = Arrays.asList(
            new UserDTO(),
            new UserDTO()
        );
        Page<UserDTO> userPage = new PageImpl<>(userDTOList);

        given(userService.getWithPagination(anyInt(), anyInt(), anyBoolean())).willReturn(userPage);

        mockMvc.perform(get("/admin"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldIsOkStatusAdminPanelRole() throws Exception {
        UserDTO userDTO = new UserDTO();
        List<Role> userRoles = Arrays.asList(Role.ADMIN, Role.STUDENT);
        List<AuthorityDTO> userAuthorities = userRoles.stream()
            .map(role -> AuthorityDTO.builder().role(role).build())
            .toList();

        userDTO.setAuthorities(userAuthorities);

        given(userService.getUserById(anyLong())).willReturn(userDTO);

        mockMvc.perform(get("/admin/users/{id}/role", 1L))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/user_role"))
            .andExpect(model().attributeExists("user", "userRoles"))
            .andExpect(model().attribute("user", userDTO))
            .andExpect(model().attribute("userRoles", userRoles));
    }
}