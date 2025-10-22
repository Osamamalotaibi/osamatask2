package com.example.taskapi.service.impl;

import com.example.taskapi.security.jwt.JwtUtil;
import com.example.taskapi.service.jwt.JwtService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;

    @Override
    public String generateAccessToken(String username) {
        // استعمل الـ username مباشرة
        return jwtUtil.generateToken(username);
    }

    @Override
    public String generateRefreshToken(String username) {
        // برضه نفس الشي: تمرر username فقط
        return jwtUtil.createRefreshToken(username);
    }
}
