package com.authentication.module.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @NotBlank
        @Size(min = 8, max = 100)
        String oldPassword,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain at least one letter, one number, and one special character")
        String newPassword,

        @NotBlank
        @Size(min = 8, max = 100)
        String confirmNewPassword
) {}