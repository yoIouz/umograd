package com.umograd.content.domain.task;

import java.util.Objects;

public record TaskTitle(String value) {
    public TaskTitle {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Title is required");
        if (value.length() > 200) throw new IllegalArgumentException("Title too long");
    }
}

