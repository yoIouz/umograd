package com.umograd.content.domain.result;

public record TaskResultId(Long value) {
    public TaskResultId {
        if (value == null || value <= 0) throw new IllegalArgumentException("TaskResultId must be positive");
    }
}
