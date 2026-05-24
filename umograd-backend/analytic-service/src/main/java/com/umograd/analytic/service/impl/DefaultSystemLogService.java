package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.SystemLogDto;
import com.umograd.analytic.entity.SystemLogEntity;
import com.umograd.analytic.repository.analytic.SystemLogRepository;
import com.umograd.analytic.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultSystemLogService implements SystemLogService {

    private final SystemLogRepository systemLogRepository;

    @Transactional
    public void logError(Long userId, String username, String endpoint, String message) {
        SystemLogEntity log = new SystemLogEntity();
        log.setUserId(userId != null ? userId : 0L);
        log.setUsername(username != null ? username : "SYSTEM");
        log.setEventType("ERROR");
        log.setDescription(message);
        log.setEndpoint(endpoint);
        log.setCreatedAt(LocalDateTime.now());
        systemLogRepository.save(log);
    }

    @Override
    public List<SystemLogDto> findLogs(String severity) {
        return systemLogRepository.findByEventTypeOrderByCreatedAtDesc(severity).stream()
                .map(log ->
                        new SystemLogDto(
                                log.getId(), log.getUserId(), log.getUsername(),
                                log.getEventType(), log.getEndpoint(),
                                log.getDescription(), log.getCreatedAt()
                        )).toList();
    }


}
