package dev.alexcoss.universitycms.service.auth;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.UserRepository;
import dev.alexcoss.universitycms.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PersonDetails(userRepository.findByUsername(username)
            .map(user -> modelMapper.map(user, UserAuthDTO.class))
            .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username)));
    }
}
