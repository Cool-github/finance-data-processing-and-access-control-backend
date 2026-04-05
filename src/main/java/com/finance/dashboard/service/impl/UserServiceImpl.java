package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.UserRequestDTO;
import com.finance.dashboard.dto.UserResponseDTO;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Role;
import com.finance.dashboard.enums.UserStatus;
import com.finance.dashboard.exception.ResourceAlreadyExistsException;
import com.finance.dashboard.exception.UnauthorizedException;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {

        log.info("Creating user with email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("User already exists with email: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        // Get current user role
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Restrict ADMIN creation
        if (request.getRole() == Role.ROLE_ADMIN && !isAdmin) {
            throw new UnauthorizedException("Only ADMIN can create ADMIN users");
        }

        // Create user AFTER validation
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Database constraint violation for email: {}", request.getEmail(), ex);
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        log.info("User created successfully with id: {}", user.getId());

        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        log.info("Fetching all users");

        // future enhancement -  add page, size instead of findall()
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    @Override
    public UserResponseDTO registerUser(UserRequestDTO request) {

        log.info("Registering user: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        // CHECK IF ADMIN EXISTS
        boolean adminExists = userRepository.existsByRole(Role.ROLE_ADMIN);

        if (adminExists) {
            // Block any further registration
            throw new UnauthorizedException("Admin already exists. Registration is closed. Please login as Admin to create new USER/ADMIN");
        }

        // FIRST AND ONLY ADMIN
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        log.info("Admin user created successfully");

        return mapToResponse(user);
    }
}
