package dev.alexcoss.universitycms.controller.user;

import dev.alexcoss.universitycms.dto.view.AuthorityDTO;
import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.dto.view.user.UserEditDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.Person;
import dev.alexcoss.universitycms.security.PersonDetails;
import dev.alexcoss.universitycms.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserDetailsController.class)
@RunWith(SpringRunner.class)
class UserDetailsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void userDetails() throws Exception {
        PersonDetails user = new PersonDetails(getUserAuthDTO());
        mockMvc.perform(get("/details").with(user(user)))
            .andExpect(status().isOk())
            .andExpect(view().name("user/details"))
            .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    void editUser() throws Exception {
        PersonDetails user = new PersonDetails(getUserAuthDTO());
        mockMvc.perform(get("/details/edit").with(user(user)))
            .andExpect(status().isOk())
            .andExpect(view().name("user/edit"))
            .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    void updateUserValidData() throws Exception {
        UserEditDTO userDTO = new UserEditDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("newpassword");

        PersonDetails currentUser = new PersonDetails(getUserAuthDTO());
        currentUser.getPerson().setUsername("testuser");
        currentUser.getPerson().setAuthorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()));

        mockMvc.perform(patch("/details").with(csrf())
                .param("current_password", "oldpassword")
                .flashAttr("user", userDTO)
                .with(user(currentUser)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?logout"));

        verify(userService, times(1)).updateUser(userDTO);
    }

    @Test
    @WithMockUser
    void updateUserInvalidPassword() throws Exception {
        UserEditDTO userDTO = new UserEditDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("newpassword");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        PersonDetails currentUser = new PersonDetails(new UserAuthDTO());
        currentUser.getPerson().setUsername("testuser");
        currentUser.getPerson().setAuthorities(List.of());

        mockMvc.perform(patch("/details").with(csrf())
                .param("current_password", "wrongpassword")
                .flashAttr("user", userDTO)
                .with(user(currentUser)))
            .andExpect(status().isOk())
            .andExpect(view().name("user/edit"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeHasFieldErrorCode("user", "password", "password.invalid"));

        verify(userService, times(0)).updateUser(userDTO);
    }

    private UserAuthDTO getUserAuthDTO(){
        return UserAuthDTO.builder()
            .username("testuser")
            .password("testpassword")
            .firstName("firstname")
            .lastName("lastname")
            .email("testemail")
            .authorities(List.of(AuthorityDTO.builder().role(Role.STUDENT).build()))
            .build();
    }
}