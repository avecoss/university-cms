package dev.alexcoss.universitycms.service.user;

import dev.alexcoss.universitycms.dto.view.user.UserDTO;
import dev.alexcoss.universitycms.dto.view.user.UserEditDTO;
import dev.alexcoss.universitycms.enumerated.Role;
import dev.alexcoss.universitycms.model.Authority;
import dev.alexcoss.universitycms.model.Student;
import dev.alexcoss.universitycms.model.Teacher;
import dev.alexcoss.universitycms.model.User;
import dev.alexcoss.universitycms.repository.AuthorityRepository;
import dev.alexcoss.universitycms.repository.StudentRepository;
import dev.alexcoss.universitycms.repository.TeacherRepository;
import dev.alexcoss.universitycms.repository.UserRepository;
import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public boolean checkPassword(String password, UserEditDTO user) {
        return passwordEncoder.matches(password, userRepository.findByUsername(user.getUsername()).map(User::getPassword).orElse(null));
    }

    @Transactional
    public void updateUser(UserEditDTO userDTO) {
        userRepository.findByUsername(userDTO.getUsername()).ifPresent(user -> {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            userRepository.save(user);
        });
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUserAuthority(Long id, List<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            userRepository.findById(id).ifPresent(user -> {
                boolean isStudent = studentRepository.existsByUserId(id);
                boolean isTeacher = teacherRepository.existsByUserId(id);

                Set<Authority> newAuthorities = roles.stream()
                    .map(role -> authorityRepository.findByRole(role)
                        .orElseThrow(() -> new IllegalEntityException("Role not found: " + role.name())))
                    .collect(Collectors.toSet());

                user.setAuthorities(newAuthorities);
                userRepository.save(user);

                checkAndDeleteRole(isStudent, isTeacher, newAuthorities, id);
                checkAndAddRole(isStudent, isTeacher, newAuthorities, user);
            });
        }
    }

    public boolean isUserByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Set<String> getAllUsernames() {
        return userRepository.findAllUsernames();
    }

    public List<UserDTO> findAllUsers(boolean sortByUsername) {
        Sort sort = sortByUsername ? Sort.by("username") : Sort.unsorted();
        List<User> users = userRepository.findAll(sort);

        return users.stream()
            .map(user -> modelMapper.map(user, UserDTO.class))
            .toList();
    }

    public Page<UserDTO> getWithPagination(Integer page, Integer usersPerPage, boolean sortByUsername) {
        Pageable pageable = PageRequest.of(page - 1, usersPerPage, sortByUsername ? Sort.by("username") : Sort.unsorted());
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDTO> userDTOs = userPage.stream()
            .map(user -> modelMapper.map(user, UserDTO.class))
            .toList();

        return new PageImpl<>(userDTOs, pageable, userPage.getTotalElements());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
            .map(user -> modelMapper.map(user, UserDTO.class))
            .orElseThrow(() -> new EntityNotExistException("User with id " + id + " not found"));
    }

    private void checkAndDeleteRole(boolean isStudent, boolean isTeacher, Set<Authority> newAuthorities, Long userId) {
        if (isStudent && newAuthorities.stream().noneMatch(auth -> auth.getRole().equals(Role.STUDENT))) {
            studentRepository.deleteByUserId(userId);
        }

        if (isTeacher && newAuthorities.stream().noneMatch(auth -> auth.getRole().equals(Role.TEACHER))) {
            teacherRepository.deleteByUserId(userId);
        }
    }

    private void checkAndAddRole(boolean isStudent, boolean isTeacher, Set<Authority> newAuthorities, User user) {
        if (!isStudent && newAuthorities.stream().anyMatch(auth -> auth.getRole().equals(Role.STUDENT))) {
            studentRepository.save(Student.builder()
                .user(user)
                .build());
        }

        if (!isTeacher && newAuthorities.stream().anyMatch(auth -> auth.getRole().equals(Role.TEACHER))) {
            teacherRepository.save(Teacher.builder()
                .user(user)
                .build());
        }
    }
}
