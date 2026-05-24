package com.umograd.content.domain.task;

public record TaskDescription(String value) {
    public TaskDescription {
        if (value == null) throw new IllegalArgumentException("Description is required");
    }
}

