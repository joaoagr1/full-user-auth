package com.authentication.module.repositories;

import com.authentication.module.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
  
    UserDetails findByLogin(String login);


    boolean existsByLogin(String login);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByLogin(String login);

    void deleteByEmail(String email);
}
