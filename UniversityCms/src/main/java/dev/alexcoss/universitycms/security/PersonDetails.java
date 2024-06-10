package dev.alexcoss.universitycms.security;

import dev.alexcoss.universitycms.dto.view.user.UserAuthDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class PersonDetails implements UserDetails {
    private final UserAuthDTO person;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return person.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getRole()))
            .toList();
    }

    public String getPassword() {
        return person.getPassword();
    }

    public String getUsername() {
        return person.getUsername();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
