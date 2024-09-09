package com.authentication.module.repositories;

import com.authentication.module.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
   Optional<PasswordResetToken> findByToken(String token);
}
