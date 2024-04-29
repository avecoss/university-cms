package dev.alexcoss.universitycms.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
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
    @Size(min = 2, max = 30, message = "{person.validation.size_username}")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "{person.validation.not_empty_pass}")
    @Size(min = 8, max = 100, message = "{person.validation.size_pass}")
    @Column(name = "password")
    private String password;
}
