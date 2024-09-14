package com.authentication.module.dtos;

import com.authentication.module.domain.Users;

public record LoginResponseDTO(String token,
                               Users users) {
}
