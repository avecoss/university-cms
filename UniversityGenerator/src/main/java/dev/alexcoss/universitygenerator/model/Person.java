package dev.alexcoss.universitygenerator.model;

import dev.alexcoss.universitygenerator.enumerated.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Person {

    @NotEmpty
    @Size(min = 2, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Column(name = "username", unique = true)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 100)
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "u_role")
    private Role role;
}
