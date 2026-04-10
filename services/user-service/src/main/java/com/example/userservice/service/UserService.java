package com.example.userservice.service;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.response.AuthResponse;

public interface UserService {
    void registerUser(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
