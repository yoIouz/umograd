package com.umograd.analytic.service;

import com.umograd.analytic.dto.SystemLogDto;

import java.util.List;

public interface SystemLogService {

    void logError(Long userId, String username, String endpoint, String message);

    List<SystemLogDto> findLogs(String severity);
}
