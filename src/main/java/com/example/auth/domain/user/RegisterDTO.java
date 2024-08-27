package com.example.auth.domain.user;

import com.example.auth.validation.DocumentValidation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(

        @NotBlank(message = "Login cannot be blank")
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters long")
        String login,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain at least one letter, one number, and one special character")
        String password,

        @Nullable
        UserRole role,

        @NotBlank(message = "Document cannot be blank")
        @DocumentValidation(message = "Invalid document")
        String document,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must be less than or equal to 100 characters")
        String email

) {

        public static RegisterDTO withDefaultRole(String login, String password, @Nullable UserRole role, String document, String email) {
                return new RegisterDTO(login, password, (role != null) ? role : UserRole.USER, document, email);
        }
}
