package com.example.auth.controllers;

import com.example.auth.domain.LoginRequestDTO;
import com.example.auth.domain.LoginResponseDTO;
import com.example.auth.domain.RegisterDTO;
import com.example.auth.domain.SuccessResponseDTO;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.UserRepository;
import com.example.auth.services.AuthorizationService;
import com.example.auth.services.CredService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO data){

      String token =  credService.login(data.login(), data.password());
      return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {

        credService.register(data);
        return ResponseEntity.ok(new SuccessResponseDTO("User created successfully."));

    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        boolean isVerified = authorizationService.verifyUser(token);
        if (isVerified) {
            return ResponseEntity.ok("Email confirmado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
        }
    }

    @GetMapping("list-users")
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(repository.findAll());
    }


}
