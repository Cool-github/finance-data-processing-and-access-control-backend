package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Role;
import com.finance.dashboard.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
}
