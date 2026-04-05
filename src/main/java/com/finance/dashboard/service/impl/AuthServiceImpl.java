package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.AuthRequestDTO;
import com.finance.dashboard.dto.AuthResponseDTO;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.UserStatus;
import com.finance.dashboard.exception.InvalidCredentialsException;
import com.finance.dashboard.exception.UnauthorizedException;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.security.JwtService;
import com.finance.dashboard.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {

        log.info("Login attempt: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed - user not found: {}", request.getEmail());
                    return new InvalidCredentialsException("Invalid email or password");
                });
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed - wrong password: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("User is inactive");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole().name());

        log.info("Login successful: {}", request.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .build();
    }
}
