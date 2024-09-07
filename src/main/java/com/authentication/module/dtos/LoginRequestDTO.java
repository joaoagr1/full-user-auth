package com.authentication.module.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotNull(message = "Login cannot be null")
        String login,

        @NotNull(message = "Password cannot be null")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password

){

}
