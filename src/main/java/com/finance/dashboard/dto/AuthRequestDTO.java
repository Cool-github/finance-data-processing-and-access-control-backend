package com.finance.dashboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
