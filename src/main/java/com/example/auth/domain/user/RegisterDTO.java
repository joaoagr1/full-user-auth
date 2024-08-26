package com.example.auth.domain.user;

import com.example.auth.validation.DocumentValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

        @NotNull(message = "Login cannot be null")
        String login,

        @NotNull(message = "Password cannot be null")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotNull(message = "Role cannot be null")
        UserRole role,

        @NotNull(message = "Document cannot be null")
        @DocumentValidation(message = "Invalid document")
        String document

) {
}
