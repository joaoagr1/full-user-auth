package com.authentication.module.controllers;

import com.authentication.module.dtos.*;
import com.authentication.module.infra.security.CustomAuthorization;
import com.authentication.module.repositories.UserRepository;
import com.authentication.module.services.LoginService;
import com.authentication.module.services.RegisterService;
import com.authentication.module.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.UID;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CustomAuthorization customAuthorization;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/confirm")
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
    public ResponseEntity<SuccessResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {

        loginService.resetPassword(resetPasswordDTO.token(), resetPasswordDTO.newPassword());
        return ResponseEntity.ok(new SuccessResponseDTO("Senha redefinida com sucesso."));
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("customAuthorization.canDeleteUser(principal, #userId)")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {

        userService.deleteUser(userId);
        return ResponseEntity.ok("Usuário excluído com sucesso.");
    }

}
