package com.finance.dashboard.controller;

import com.finance.dashboard.dto.ApiResponse;
import com.finance.dashboard.dto.AuthRequestDTO;
import com.finance.dashboard.dto.AuthResponseDTO;
import com.finance.dashboard.service.AuthService;
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

    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ApiResponse.<AuthResponseDTO>builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }
}