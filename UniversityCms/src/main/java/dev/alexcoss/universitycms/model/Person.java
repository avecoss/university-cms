package dev.alexcoss.universitycms.model;

import dev.alexcoss.universitycms.enumerated.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class Person {

    @NotEmpty(message = "{person.validation.not_empty_firstname}")
    @Size(min = 2, max = 50, message = "{person.validation.size_firstname}")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "{person.validation.not_empty_lastname}")
    @Size(min = 2, max = 50, message = "{person.validation.size_lastname}")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "{person.validation.not_empty_username}")
    @Size(min = 2, max = 100, message = "{person.validation.size_username}")
    @Column(name = "username", unique = true)
    private String username;

    @NotEmpty(message = "{person.validation.not_empty_pass}")
    @Size(min = 8, max = 100, message = "{person.validation.size_pass}")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "u_role")
    private Role role;
}
