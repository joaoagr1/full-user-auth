package com.authentication.module.dtos;

import com.authentication.module.domain.User;

public record LoginResponseDTO(String token,
                               User user) {
}
