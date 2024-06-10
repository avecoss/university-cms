package dev.alexcoss.universitycms.service.auth;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.AuthorityRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RegistrationServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthorityRepository authorityRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationService registrationService;

    @Test
    void testRegister() {
        UserAuthDTO authDTO = new UserAuthDTO();
        authDTO.setUsername("testUser");
        authDTO.setPassword("testPassword");

        User user = new User();
        user.setUsername("testUser");

        Authority authority = Authority.builder().role(Role.STUDENT).build();

        when(modelMapper.map(any(UserAuthDTO.class), eq(User.class))).thenReturn(user);
        when(authorityRepository.findByRole(Role.STUDENT)).thenReturn(Optional.of(authority));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        registrationService.register(authDTO);

        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getAuthorities().contains(authority));

        verify(modelMapper, times(1)).map(authDTO, User.class);
        verify(authorityRepository, times(1)).findByRole(Role.STUDENT);
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userRepository, times(1)).save(user);
    }
}