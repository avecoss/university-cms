package dev.alexcoss.universitycms.controller.auth;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.service.auth.PersonDetailsService;
import dev.alexcoss.universitycms.service.auth.RegistrationService;
import dev.alexcoss.universitycms.util.PersonValidator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@RunWith(SpringRunner.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonValidator personValidator;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void loginShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void registrationGetShouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/registration"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registration"));
    }

    @Test
    @WithMockUser
    void registerPostShouldRedirectToLoginOnSuccess() throws Exception {
        UserAuthDTO user = new UserAuthDTO();
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");

        doNothing().when(personValidator).validate(any(UserAuthDTO.class), any(BindingResult.class));

        mockMvc.perform(post("/registration")
                .flashAttr("person", user)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));

        verify(registrationService).register(user);
    }

    @Test
    @WithMockUser
    void registerPostShouldReturnRegistrationViewOnValidationError() throws Exception {
        UserAuthDTO person = new UserAuthDTO();
        doAnswer(invocation -> {
            BindingResult bindingResult = invocation.getArgument(1);
            bindingResult.reject("error");
            return null;
        }).when(personValidator).validate(any(UserAuthDTO.class), any(BindingResult.class));

        mockMvc.perform(post("/registration")
                .flashAttr("person", person)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registration"));

        verify(registrationService, never()).register(any(UserAuthDTO.class));
    }
}