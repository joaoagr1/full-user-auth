package com.example.auth.repositories;

import com.example.auth.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(String login);

    User findByDocument(String document);

    User findByEmail(String email);

    boolean existsByLogin(String login);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);
}
