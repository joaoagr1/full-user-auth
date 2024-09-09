package com.authentication.module.services;

import com.authentication.module.domain.User;
import com.authentication.module.dtos.UpdatePasswordDTO;
import com.authentication.module.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void deleteUser(String username, String requestingUserEmail) {
        Optional<User> deletedUser = userRepository.findUserByLogin(username);

        if (deletedUser.get().getLogin().equals(requestingUserEmail) || isAdmin(requestingUserEmail)) {
            userRepository.deleteByLogin(username);
        } else {
            throw new AccessDeniedException("Usuário não autorizado para deletar este usuário.");
        }
    }

    private boolean isAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> "ADMIN".equals(user.getRole()))
                .orElse(false);
    }

    public void updatePassword(String username, String requestingUserName, @Valid UpdatePasswordDTO updatePasswordDTO) {
        Optional<User> userOptional = userRepository.findUserByLogin(username);

        if (!updatePasswordDTO.newPassword().equals(updatePasswordDTO.confirmNewPassword())) {
            throw new AccessDeniedException("As senhas não coincidem.");
        }

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getLogin().equals(requestingUserName) || isAdmin(requestingUserName)) {
                if (passwordEncoder.matches(updatePasswordDTO.oldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(updatePasswordDTO.newPassword()));
                    userRepository.save(user);
                } else {
                    throw new AccessDeniedException("Senha antiga incorreta.");
                }
            } else {
                throw new AccessDeniedException("Usuário não autorizado para alterar a senha deste usuário.");
            }
        } else {
            throw new AccessDeniedException("Usuário não encontrado.");
        }
    }
}