package com.umograd.content.domain.task;

import java.util.Objects;

public record TaskId(Long value) {
    public TaskId {
        if (value == null || value <= 0) throw new IllegalArgumentException("TaskId must be positive");
    }
}

