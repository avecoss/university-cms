package dev.alexcoss.universitycms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user", schema = "university")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

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
    @Size(min = 6, max = 100, message = "{person.validation.size_pass}")
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
        name = "user_authority",
        schema = "university",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.getUsers().add(this);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        authority.getUsers().remove(this);
    }
}
