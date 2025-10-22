package com.example.taskapi.service.impl;

import com.example.taskapi.dto.AuthRequest;
import com.example.taskapi.dto.AuthResponse;
import com.example.taskapi.entity.User;
import com.example.taskapi.repository.UserRepository;
import com.example.taskapi.service.AuthService;
import com.example.taskapi.service.jwt.JwtService;
import com.example.taskapi.security.PasswordService;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordService passwordEncoder; // من Micronaut (micronaut-security-crypto)
    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String access  = jwtService.generateAccessToken(user.getUsername());
        String refresh = jwtService.generateRefreshToken(user.getUsername());

        return new AuthResponse(access, refresh);
    }
}
