package com.finance.dashboard.repository.impl;

import com.finance.dashboard.dto.UserRequestDTO;
import com.finance.dashboard.dto.UserResponseDTO;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.UserStatus;
import com.finance.dashboard.exception.ResourceAlreadyExistsException;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {

        log.info("Creating user with email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("User already exists with email: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // later → encode
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        log.info("User created successfully with id: {}", user.getId());

        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        log.info("Fetching all users");

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
}
