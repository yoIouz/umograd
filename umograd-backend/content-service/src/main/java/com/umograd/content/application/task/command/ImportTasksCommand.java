package com.umograd.content.application.task.command;

public record ImportTasksCommand(String providerName, String topic, int limit, String createdBy) {}
