package com.authentication.module.repositories;

import com.authentication.module.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, String> {
    User findByLogin(String login);

}