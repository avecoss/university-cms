package dev.alexcoss.universitycms.service.auth;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.AuthorityRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserAuthDTO authDTO) {
        User user = modelMapper.map(authDTO, User.class);
        user.addAuthority(authorityRepository.findByRole(Role.STUDENT)
                .orElse(Authority.builder().role(Role.STUDENT).build()));
        user.setPassword(passwordEncoder.encode(authDTO.getPassword()));

        userRepository.save(user);
    }
}
