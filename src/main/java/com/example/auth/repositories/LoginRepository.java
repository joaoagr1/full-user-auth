package com.example.auth.repositories;

import com.example.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, String> {
    User findByLogin(String login);

}