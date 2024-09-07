package com.example.auth.services;

import com.example.auth.domain.RegisterDTO;
import com.example.auth.domain.User;
import com.example.auth.exceptions.DocumentAlreadyExistsException;
import com.example.auth.exceptions.EmailAlreadyExistsException;
import com.example.auth.exceptions.LoginAlreadyExistsException;
import com.example.auth.exceptions.UserNotVerifiedException;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.LoginRepository;
import com.example.auth.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Service
public class CredService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private EmailService emailService;

    public String login(String login, String password) {
        User user = findUserByLogin(login);
        verifyEmail(user);
        Authentication auth = authenticateUser(login, password);
        return generateToken(auth);
    }

    public void register(RegisterDTO data) {
        validateRegistrationData(data);
        User newUser = createUser(data);
        saveUser(newUser);
        sendVerificationEmail(newUser);
    }

    private User findUserByLogin(String login) {
        return Optional.ofNullable(loginRepository.findByLogin(login))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void verifyEmail(User user) {
        if (!user.isEmailVerified()) {
            throw new UserNotVerifiedException("Email not verified");
        }
    }

    private Authentication authenticateUser(String login, String password) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        return authenticationManager.authenticate(usernamePassword);
    }

    private String generateToken(Authentication auth) {
        return tokenService.generateToken((User) auth.getPrincipal());
    }

    private void validateRegistrationData(RegisterDTO data) {
        Map<BooleanSupplier, Supplier<RuntimeException>> validations = Map.of(
                () -> repository.existsByLogin(data.login()), () -> new LoginAlreadyExistsException("Login already exists."),
                () -> repository.existsByDocument(data.document()), () -> new DocumentAlreadyExistsException("Document already in use."),
                () -> repository.existsByEmail(data.email()), () -> new EmailAlreadyExistsException("Email already in use.")
        );

        validations.forEach((condition, exceptionSupplier) -> {
            if (condition.getAsBoolean()) throw exceptionSupplier.get();
        });
    }

    private User createUser(RegisterDTO data) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role(), data.document(), data.email());

        return newUser;
    }

    private void saveUser(User newUser) {
        repository.save(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        try {
            emailService.sendVerificationEmail(newUser.getEmail(), newUser.getId());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email.", e);
        }
    }
}