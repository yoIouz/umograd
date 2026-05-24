package com.umograd.analytic.controller;

import com.umograd.analytic.dto.SessionStatusDto;
import com.umograd.analytic.dto.SystemLogDto;
import com.umograd.analytic.entity.limit.ParentAgeLimitEntity;
import com.umograd.analytic.entity.limit.ParentChildCustomLimitEntity;
import com.umograd.analytic.repository.limit.ParentAgeLimitRepository;
import com.umograd.analytic.repository.analytic.UserAnalyticsRepository;
import com.umograd.analytic.repository.limit.ParentChildCustomLimitRepository;
import com.umograd.analytic.security.AuthenticationHolder;
import com.umograd.analytic.service.SystemLogService;
import com.umograd.analytic.util.GroupExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics/logs")
public class SystemLogController {

    private final StringRedisTemplate redisTemplate;

    private final SystemLogService service;

    private final ParentAgeLimitRepository limitRepository;

    private final UserAnalyticsRepository userAnalyticsRepository;

    private final ParentChildCustomLimitRepository customLimitRepository;

    private static final String REDIS_PREFIX = "active_session:";
    private static final String REDIS_TIME_PREFIX = "heartbeat:time:daily:";

    @GetMapping("/monitoring/errors")
    public List<SystemLogDto> getErrorLogs() {
        return service.findLogs("ERROR");
    }

    @PostMapping("/monitoring/heartbeat/child")
    public SessionStatusDto receiveHeartbeat(
            @RequestHeader("Authorization") String token,
            @RequestParam int age) {

        String cleanToken = token.replace("Bearer ", "");
        String activeKey = REDIS_PREFIX + cleanToken;
        redisTemplate.opsForValue().set(activeKey, "active", 15, TimeUnit.SECONDS);

        String dateSuffix = LocalDate.now().toString();
        String timeKey = REDIS_TIME_PREFIX + AuthenticationHolder.getUserId() + ":" + dateSuffix;

        redisTemplate.opsForValue().increment(timeKey, 10);
        redisTemplate.expire(timeKey, 25, TimeUnit.HOURS);

        String rawSeconds = redisTemplate.opsForValue().get(timeKey);
        long secondsUsed = rawSeconds != null ? Long.parseLong(rawSeconds) : 0;
        long minutesUsedToday = secondsUsed / 60;

        long parentId = userAnalyticsRepository.getParentId(AuthenticationHolder.getUserId());
        Optional<ParentChildCustomLimitEntity> customOpt =
                customLimitRepository.findByParentIdAndChildId(parentId, AuthenticationHolder.getUserId());

        int maxMinutes;
        if (customOpt.isPresent()) {
            maxMinutes = customOpt.get().getCustomMinutes();
        } else {
            int targetAgeGroup = GroupExecutor.getTargetAgeGroup(age);
            maxMinutes = limitRepository.findByParentIdAndAge(parentId, targetAgeGroup)
                    .map(ParentAgeLimitEntity::getMaxMinutes)
                    .orElse(0);
        }

        if (minutesUsedToday >= maxMinutes) {
            return new SessionStatusDto("BLOCKED", "Время сессии исчерпано согласно настройкам родителя.", 0);
        }

        return new SessionStatusDto("ACTIVE", "Сессия active", maxMinutes - minutesUsedToday);
    }

    @PostMapping("/monitoring/heartbeat")
    @PreAuthorize("hasRole('MODERATOR')")
    public void receiveHeartbeatMod(@RequestHeader("Authorization") String token) {
        if (token != null) {
            String key = REDIS_PREFIX + token.replace("Bearer ", "");
            redisTemplate.opsForValue().set(key, "active", 15, TimeUnit.SECONDS);
        }
    }

    @GetMapping("/monitoring/active-sessions")
    public Map<String, Integer> getActiveSessionsCount() {
        Set<String> keys = redisTemplate.keys(REDIS_PREFIX + "*");
        int count = keys.size();

        if (count == 0) count = 1;

        return Map.of("count", count);
    }
}
