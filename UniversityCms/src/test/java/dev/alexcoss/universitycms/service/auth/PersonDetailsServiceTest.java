package dev.alexcoss.universitycms.service.auth;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonDetailsServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private PersonDetailsService personDetailsService;

    @Test
    void shouldReturnUserDetails() {
        User user = new User();
        user.setUsername("testUser");

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setUsername("testUser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(any(User.class), eq(UserAuthDTO.class))).thenReturn(userAuthDTO);

        UserDetails userDetails = personDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(modelMapper, times(1)).map(user, UserAuthDTO.class);
    }

    @Test
    void shouldNotReturnUserDetails() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
            personDetailsService.loadUserByUsername("nonExistingUser"));

        verify(userRepository, times(1)).findByUsername("nonExistingUser");
        verify(modelMapper, times(0)).map(any(User.class), eq(UserAuthDTO.class));
    }
}