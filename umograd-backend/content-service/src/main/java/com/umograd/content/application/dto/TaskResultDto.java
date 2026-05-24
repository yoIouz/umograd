package com.umograd.content.application.dto;

public record TaskResultDto(Long id, Long taskId, Long childId,
                            String status, Integer score,
                            String finishedAt, String startedAt) {}
