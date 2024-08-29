package com.example.auth.services;

import com.example.auth.domain.user.User;
import com.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public boolean verifyUser(String token) {
        User user = userRepository.findById(token)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmailVerified(true);
        userRepository.save(user);

        return true;
    }
}