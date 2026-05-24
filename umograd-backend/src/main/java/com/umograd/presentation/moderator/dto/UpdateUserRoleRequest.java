package com.umograd.presentation.moderator.dto;

import com.umograd.domain.user.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull Role role
) {}
