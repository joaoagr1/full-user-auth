package com.authentication.module.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank(message = "Login cannot be blank")
        String login,

        @NotBlank(message = "Password cannot be blank")
        String password

){

}
