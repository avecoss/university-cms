package dev.alexcoss.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Person {

    @NotEmpty(message = "First name should not be empty")
    @Size(min = 2, max = 100, message = "First name should be between 2 and 100 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @Size(min = 2, max = 100, message = "Last name should be between 2 and 100 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "Username should be between 2 and 100 characters")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 250, message = "Password should be between 8 and 250 characters")
    @Column(name = "password")
    private String password;
}
