package dev.alexcoss.universitycms.service.user;

import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.AuthorityRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @Test
    void updateUserAuthority() {
        Long userId = 1L;
        List<Role> roles = Arrays.asList(Role.STUDENT, Role.ADMIN);

        User user = new User();
        user.setId(userId);

        Authority userAuthority = new Authority();
        userAuthority.setRole(Role.STUDENT);
        Authority adminAuthority = new Authority();
        adminAuthority.setRole(Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(authorityRepository.findByRole(any())).thenAnswer(invocation -> {
            Role role = invocation.getArgument(0);
            if (role == Role.STUDENT) return Optional.of(userAuthority);
            else if (role == Role.ADMIN) return Optional.of(adminAuthority);
            else return Optional.empty();
        });

        userService.updateUserAuthority(userId, roles);

        verify(userRepository, times(1)).findById(userId);
        verify(authorityRepository, times(1)).findByRole(Role.STUDENT);
        verify(authorityRepository, times(1)).findByRole(Role.ADMIN);

        assertTrue(user.getAuthorities().contains(userAuthority));
        assertTrue(user.getAuthorities().contains(adminAuthority));
    }

    @Test
    void findWithPaginationWithValidParameters() {
        int page = 1;
        int usersPerPage = 10;
        boolean sortByUsername = true;

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("user" + i);
            users.add(user);
        }

        Pageable pageable = PageRequest.of(page - 1, usersPerPage, sortByUsername ? Sort.by("username") : Sort.unsorted());
        Page<User> userPage = new PageImpl<>(users, pageable, 20);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserDTO> resultPage = userService.findWithPagination(page, usersPerPage, sortByUsername);

        assertEquals(users.size(), resultPage.getContent().size());
        assertEquals(users.getFirst().getId(), resultPage.getContent().getFirst().getId());

        verify(userRepository, times(1)).findAll(pageable);
    }
}