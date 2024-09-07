package com.example.auth.repositories;

import com.example.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(String login);


    boolean existsByLogin(String login);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);

}
