package com.umograd.presentation.moderator.dto;

import com.umograd.domain.user.Role;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        Set<Role> roles
) {}
