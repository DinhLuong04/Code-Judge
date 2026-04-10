package com.example.userservice.service;

import com.example.userservice.dto.request.RegisterRequest;

public interface UserService {
    void registerUser(RegisterRequest request);
}
