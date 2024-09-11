package com.authentication.module.repositories;

import com.authentication.module.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Users, String> {
    Users findByLogin(String login);

}