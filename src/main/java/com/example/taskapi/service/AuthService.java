package com.example.taskapi.service;

import com.example.taskapi.dto.AuthRequest;
import com.example.taskapi.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
