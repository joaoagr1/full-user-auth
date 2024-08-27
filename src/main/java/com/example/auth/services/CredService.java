package com.example.auth.services;

import com.example.auth.domain.user.RegisterDTO;
import com.example.auth.domain.user.User;
import com.example.auth.exceptions.DocumentAlreadyExistsException;
import com.example.auth.exceptions.EmailAlreadyExistsException;
import com.example.auth.exceptions.LoginAlreadyExistsException;
import com.example.auth.exceptions.UserNotVerifiedException;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.LoginRepository;
import com.example.auth.repositories.UserRepository;
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

    public String login(String login, String password) {

        //is verified

        User user = Optional.ofNullable(loginRepository.findByLogin(login))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEmailVerified()) {
            throw new UserNotVerifiedException("Email not verified");
        }

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(login, password);

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return token;

    }

    public void register(RegisterDTO data) {

        Map<BooleanSupplier, Supplier<RuntimeException>> validations = Map.of(

                () -> repository.existsByLogin(data.login()),
                () -> new LoginAlreadyExistsException("Login already exists."),
                () -> repository.existsByDocument(data.document()),
                () -> new DocumentAlreadyExistsException("Document already in use."),
                () -> repository.existsByEmail(data.email()),
                () -> new EmailAlreadyExistsException("Email already in use."));

        validations.forEach((condition, exceptionSupplier) -> {
            if (condition.getAsBoolean()) throw exceptionSupplier.get();
        });

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role(), data.document(), data.email());

        repository.save(newUser);
        System.out.println("User registered: " + newUser.getRole());
    }
}