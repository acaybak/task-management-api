
package com.company.taskmanagement.service;

import com.company.taskmanagement.dto.RegisterRequestDto;

public interface UserService {
    void registerUser(RegisterRequestDto registerRequest);
}