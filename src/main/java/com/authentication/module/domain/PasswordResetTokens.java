package com.authentication.module.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "password_reset_tokens")
@Entity(name = "PasswordResetTokens")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordResetTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Users users;

    private LocalDateTime expiryDate;

    public PasswordResetTokens(String token, Users users) {
        this.token = token;
        this.users = users;
        this.expiryDate = LocalDateTime.now().plusSeconds(1); // Token válido por 1 hora
    }

}
