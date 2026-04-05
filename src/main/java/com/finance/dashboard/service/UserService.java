package com.finance.dashboard.service;

import com.finance.dashboard.dto.UserRequestDTO;
import com.finance.dashboard.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO registerUser(UserRequestDTO request);

    List<UserResponseDTO> getAllUsers();
}
