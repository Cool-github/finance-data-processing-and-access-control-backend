package com.finance.dashboard.service;

import com.finance.dashboard.dto.AuthRequestDTO;
import com.finance.dashboard.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO request);
}
