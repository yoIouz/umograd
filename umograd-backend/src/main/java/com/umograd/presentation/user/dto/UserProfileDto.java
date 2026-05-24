package com.umograd.presentation.user.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDate birthDate;
    private String avatarUrl;
}
