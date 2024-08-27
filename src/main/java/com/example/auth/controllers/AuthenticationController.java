package com.example.auth.controllers;

import com.example.auth.domain.user.*;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.UserRepository;
import com.example.auth.services.AuthorizationService;
import com.example.auth.services.CredService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CredService credService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){



      String token =  credService.login(data.login(), data.password());

      return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        credService.register(data);
        return ResponseEntity.ok(new AuthenticationResponseDTO("User created successfully."));
    }

}
