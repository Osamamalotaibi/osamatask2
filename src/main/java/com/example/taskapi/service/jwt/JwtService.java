package com.example.taskapi.service.jwt;

public interface JwtService {
    String generateAccessToken(String username);
    String generateRefreshToken(String username);
}
