package com.authentication.module.infra.security;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthorization {

    public boolean canDeleteUser(Authentication authentication, Long userId) {

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        String currentUserName = authentication.getName();
        return currentUserName.equals(userId.toString());
    }

}
