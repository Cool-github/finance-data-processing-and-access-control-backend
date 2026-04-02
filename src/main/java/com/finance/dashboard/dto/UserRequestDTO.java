package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Role;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
