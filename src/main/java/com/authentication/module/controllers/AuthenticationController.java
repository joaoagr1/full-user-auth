package com.authentication.module.controllers;

import com.authentication.module.dtos.LoginRequestDTO;
import com.authentication.module.dtos.LoginResponseDTO;
import com.authentication.module.dtos.RegisterDTO;
import com.authentication.module.dtos.SuccessResponseDTO;
import com.authentication.module.repositories.UserRepository;
import com.authentication.module.services.LoginService;
import com.authentication.module.services.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        String token = loginService.login(data.login(), data.password());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        registerService.register(data);
        return ResponseEntity.ok(new SuccessResponseDTO("User created successfully."));
    }

    @PostMapping("/confirm")
    public ResponseEntity<SuccessResponseDTO> confirmEmail(@RequestParam("token") String token) {
        registerService.verifyUser(token);
        return ResponseEntity.ok(new SuccessResponseDTO("Email confirmado com sucesso!"));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponseDTO> forgotPassword(@RequestParam("email") String email) {
        loginService.generatePasswordResetToken(email);
        return ResponseEntity.ok(new SuccessResponseDTO("Token de recuperação de senha enviado para o e-mail."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        loginService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}
