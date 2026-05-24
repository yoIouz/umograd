package com.umograd.content.application.result.command;

public record FinishTaskCommand(
        Long taskResultId,
        Integer score,
        Integer attempts,
        boolean allowZero,
        Long childId
) {}
