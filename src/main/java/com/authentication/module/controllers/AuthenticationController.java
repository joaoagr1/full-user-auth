package com.authentication.module.controllers;

import com.authentication.module.dtos.*;
import com.authentication.module.services.LoginService;
import com.authentication.module.services.RegisterService;
import com.authentication.module.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private LoginService loginService;


    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        LoginResponseDTO response = loginService.login(data.login(), data.password());
        return ResponseEntity.ok(response);
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


    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponseDTO> deleteUser(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestingUserName = authentication.getName();
        userService.deleteUser(username, requestingUserName);
        return ResponseEntity.ok(new SuccessResponseDTO("Usuário deletado com sucesso."));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    @PutMapping("/update-password")
    public ResponseEntity<SuccessResponseDTO> updatePassword(@RequestParam("username") String username, @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestingUserName = authentication.getName();
        userService.updatePassword(username, requestingUserName, updatePasswordDTO);
        return ResponseEntity.ok(new SuccessResponseDTO("Senha atualizada com sucesso."));
    }



}
