package com.umograd.application.user.command;

public record CreateChildCommand(Long parentId, String username, String email, String password) {}
