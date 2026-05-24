package com.umograd.application.user.command;

import com.umograd.domain.user.Role;

public record UpdateUserRoleCommand(Long userId, Role newRole) {}
