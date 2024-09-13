package com.authentication.module.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotNull(message = "Login cannot be null")
        String login,

        @NotNull(message = "Password cannot be null")
        String password

){

}
