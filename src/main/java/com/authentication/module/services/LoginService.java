package com.authentication.module.services;

import com.authentication.module.domain.PasswordResetTokens;
import com.authentication.module.domain.Users;
import com.authentication.module.dtos.LoginResponseDTO;
import com.authentication.module.exceptions.custom.InvalidTokenException;
import com.authentication.module.exceptions.custom.UserNotVerifiedException;
import com.authentication.module.repositories.LoginRepository;
import com.authentication.module.repositories.PasswordResetTokenRepository;
import com.authentication.module.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public LoginResponseDTO login(String identifier, String password) {

        Optional<Users> user = userRepository.findUserByLogin(identifier);

        if (user.isEmpty()) {
            user = userRepository.findByEmail(identifier);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("User not found with the username or email: " + identifier);
            }
        }

        Authentication auth = authenticateUser(user.get().getLogin(), password);

        verifyEmail(user.get());


        return new LoginResponseDTO(generateToken(auth),user.get());
    }


    @SneakyThrows
    public void generatePasswordResetToken(String email) {
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with the email: " + email);
        }

        Users users = userOptional.get();
        String token = UUID.randomUUID().toString();
        PasswordResetTokens passwordResetTokens = new PasswordResetTokens(token, users);

        tokenRepository.save(passwordResetTokens);


        emailService.sendPasswordResetEmail(users.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetTokens resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired token", null));

        Users users = resetToken.getUsers();
        users.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(users);


        tokenRepository.delete(resetToken);
    }

    private void verifyEmail(Users users) {
        if (!users.isEmailVerified()) {
            throw new UserNotVerifiedException("Email not verified");
        }
    }

    private Authentication authenticateUser(String login, String password) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        return authenticationManager.authenticate(usernamePassword);
    }

    private String generateToken(Authentication auth) {
        return tokenService.generateToken((Users) auth.getPrincipal());
    }
}
