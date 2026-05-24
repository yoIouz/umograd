package com.umograd.presentation.auth.dto;

import com.umograd.domain.user.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private Boolean isParent;
    private LocalDate birthDate;
    private String parentUsername;
    private String avatarUrl;
}
