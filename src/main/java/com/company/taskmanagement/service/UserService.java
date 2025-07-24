package com.company.taskmanagement.service;

import com.company.taskmanagement.dto.AuthResponseDto;
import com.company.taskmanagement.dto.LoginRequestDto;
import com.company.taskmanagement.dto.RegisterRequestDto;

public interface UserService {
    void registerUser(RegisterRequestDto registerRequest);

    AuthResponseDto loginUser(LoginRequestDto loginRequest);
}