package com.example.auth.services;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.auth.domain.PasswordResetToken;
import com.example.auth.domain.User;
import com.example.auth.exceptions.InvalidTokenException;
import com.example.auth.exceptions.UserNotVerifiedException;
import com.example.auth.infra.security.TokenService;
import com.example.auth.repositories.LoginRepository;
import com.example.auth.repositories.PasswordResetTokenRepository;
import com.example.auth.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private UserRepository repository;

    @Autowired
    private LoginRepository loginRepository;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public String login(String login, String password) {
        User user = findUserByLogin(login);
        verifyEmail(user);
        Authentication auth = authenticateUser(login, password);
        return generateToken(auth);
    }

    public void generatePasswordResetToken(String email) {
        Optional<User> userOptional = repository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email);
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);

        tokenRepository.save(passwordResetToken);

        // Enviar e-mail com o token
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Redefinição de Senha");
        mailMessage.setText("Para redefinir sua senha, use o token: " + token);
        mailSender.send(mailMessage);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token inválido ou expirado.", null));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        // Remover o token após a redefinição da senha
        tokenRepository.delete(resetToken);
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
}
