package com.finance.dashboard.controller;

import com.finance.dashboard.dto.*;
import com.finance.dashboard.service.AuthService;
import com.finance.dashboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {

        return ApiResponse.<AuthResponseDTO>builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO request) {
        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("User registered successfully")
                .data(userService.registerUser(request))
                .build();
    }
}