package com.authentication.module.repositories;

import com.authentication.module.domain.PasswordResetTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokens, String> {
   Optional<PasswordResetTokens> findByToken(String token);
}
