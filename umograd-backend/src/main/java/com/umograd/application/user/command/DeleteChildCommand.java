package com.umograd.application.user.command;

public record DeleteChildCommand(Long parentId, Long childId) {}
