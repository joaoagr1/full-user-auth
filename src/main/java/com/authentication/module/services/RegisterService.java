package com.authentication.module.services;

import com.authentication.module.domain.Users;
import com.authentication.module.dtos.RegisterDTO;
import com.authentication.module.exceptions.custom.DocumentAlreadyExistsException;
import com.authentication.module.exceptions.custom.EmailAlreadyExistsException;
import com.authentication.module.exceptions.custom.LoginAlreadyExistsException;
import com.authentication.module.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepositoryepository;

    @Autowired
    private EmailService emailService;

    @SneakyThrows
    public void register(RegisterDTO data) {

        validateRegistrationData(data);
        Users newUsers = createUser(data);
        userRepositoryepository.save(newUsers);
        emailService.sendVerificationEmail(newUsers.getEmail(), newUsers.getId());    }

    private void validateRegistrationData(RegisterDTO data) {
        Map<BooleanSupplier, Supplier<RuntimeException>> validations = Map.of(
                () -> userRepositoryepository.existsByLogin(data.login()), () -> new LoginAlreadyExistsException("Login already exists."),
                () -> userRepositoryepository.existsByDocument(data.document()), () -> new DocumentAlreadyExistsException("Document already in use."),
                () -> userRepositoryepository.existsByEmail(data.email()), () -> new EmailAlreadyExistsException("Email already in use.")
        );

        validations.forEach((condition, exceptionSupplier) -> {
            if (condition.getAsBoolean()) throw exceptionSupplier.get();
        });
    }

    private Users createUser(RegisterDTO data) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        return new Users(data.login(), encryptedPassword, data.role(), data.document(), data.email());
    }



    public boolean verifyUser(String token) {

        Users users = userRepositoryepository.findById(token)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        users.setEmailVerified(true);
        userRepositoryepository.save(users);

        return true;
    }

}
