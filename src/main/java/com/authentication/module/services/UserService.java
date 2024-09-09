package com.authentication.module.services;

import com.authentication.module.domain.User;
import com.authentication.module.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verifique se o papel do usuário é ADMIN
            return "ADMIN".equals(user.getRole());
        }
        return false;
    }
}
