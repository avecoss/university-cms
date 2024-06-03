package dev.alexcoss.universitycms.controller.auth;

import dev.alexcoss.universitycms.dto.view.users.PersonAuthDTO;
import dev.alexcoss.universitycms.service.auth.PersonDetailsService;
import dev.alexcoss.universitycms.service.auth.RegistrationService;
import dev.alexcoss.universitycms.util.PersonValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonValidator personValidator;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private PersonDetailsService personDetailsService;

    @Test
    void loginShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/login"));
    }

    @Test
    void registrationGetShouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/registration"))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registration"));
    }

    @Test
    void registerPostShouldRedirectToLoginOnSuccess() throws Exception {
        PersonAuthDTO person = new PersonAuthDTO();
        doNothing().when(personValidator).validate(any(PersonAuthDTO.class), any(BindingResult.class));

        mockMvc.perform(post("/registration")
                .flashAttr("person", person)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));

        verify(registrationService).register(person);
    }

    @Test
    void registerPostShouldReturnRegistrationViewOnValidationError() throws Exception {
        PersonAuthDTO person = new PersonAuthDTO();
        doAnswer(invocation -> {
            BindingResult bindingResult = invocation.getArgument(1);
            bindingResult.reject("error");
            return null;
        }).when(personValidator).validate(any(PersonAuthDTO.class), any(BindingResult.class));

        mockMvc.perform(post("/registration")
                .flashAttr("person", person)
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("auth/registration"));

        verify(registrationService, never()).register(any(PersonAuthDTO.class));
    }
}