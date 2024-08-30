package com.example.auth.domain.user;

import com.example.auth.validation.DocumentValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotBlank
    private String id;

    @NotBlank
    @Column(unique = true)
    @Size(min = 4, max = 50)
    private String login;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    @DocumentValidation
    @NotBlank
    private String document;

    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private boolean emailVerified = false;



    public User(String login, String encryptedPassword, UserRole role, String document, String email) {
        this.login = login;
        this.password = encryptedPassword;
        this.role = (role != null) ? role : UserRole.USER; // Define role as UserRole.USER if not provided
        this.document = document;
        this.email = email;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return login;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
