package com.authentication.module.services;

import com.authentication.module.dtos.PasswordResetToken;
import com.authentication.module.domain.User;
import com.authentication.module.exceptions.custom.InvalidTokenException;
import com.authentication.module.exceptions.custom.UserNotVerifiedException;
import com.authentication.module.infra.security.TokenService;
import com.authentication.module.repositories.LoginRepository;
import com.authentication.module.repositories.PasswordResetTokenRepository;
import com.authentication.module.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;


    public String login(String login, String password) {

        Optional<User> user = userRepository.findUserByLogin(login);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o login: " + login);
        }

        verifyEmail(user.get());

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
