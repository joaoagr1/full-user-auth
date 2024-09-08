// src/main/java/com/authentication/module/services/UserService.java
package com.authentication.module.services;

import com.authentication.module.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.server.UID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUser(String id) {

        userRepository.deleteById(id);
    }
}