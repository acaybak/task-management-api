package com.company.taskmanagement.service.impl;

import com.company.taskmanagement.domain.User;
import com.company.taskmanagement.dto.AuthResponseDto;
import com.company.taskmanagement.dto.LoginRequestDto;
import com.company.taskmanagement.dto.RegisterRequestDto;
import com.company.taskmanagement.repository.UserRepository;
import com.company.taskmanagement.service.JwtService;
import com.company.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void registerUser(RegisterRequestDto registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        User user = new User();
        user.setName(registerRequest.name());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        userRepository.save(user);
    }

    @Override
    public AuthResponseDto loginUser(LoginRequestDto loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalStateException("User not found after successful authentication."));

        var jwtToken = jwtService.generateToken(user);

        return new AuthResponseDto(jwtToken);
    }
}
