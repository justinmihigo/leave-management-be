package com.leave.management.leave_mgmt.services;

import com.leave.management.leave_mgmt.models.User;
import com.leave.management.leave_mgmt.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public String getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getId(); // Return userId as a String
        }
        throw new IllegalArgumentException("User not found with email: " + email);
    }
}