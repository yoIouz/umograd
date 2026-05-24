package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.ParentAgeLimitCustomResponse;
import com.umograd.analytic.dto.ParentAgeLimitResponse;
import com.umograd.analytic.dto.SessionStatusDto;
import com.umograd.analytic.entity.limit.ParentAgeLimitEntity;
import com.umograd.analytic.entity.limit.ParentChildCustomLimitEntity;
import com.umograd.analytic.mapper.LimitMapper;
import com.umograd.analytic.repository.limit.ParentAgeLimitRepository;
import com.umograd.analytic.repository.limit.ParentChildCustomLimitRepository;
import com.umograd.analytic.security.AuthenticationHolder;
import com.umograd.analytic.service.LimitService;
import com.umograd.analytic.util.GroupExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultLimitService implements LimitService {

    private final StringRedisTemplate redisTemplate;

    private final ParentAgeLimitRepository limitRepository;

    private final LimitMapper limitMapper;

    private final ParentChildCustomLimitRepository customLimitRepository;

    @Override
    public SessionStatusDto checkLimit(Long childId, Long parentId, int age) {
        String dateSuffix = LocalDate.now().toString();
        String timeKey = "heartbeat:time:daily:" + childId + ":" + dateSuffix;

        String rawSeconds = redisTemplate.opsForValue().get(timeKey);
        long secondsUsed = rawSeconds != null ? Long.parseLong(rawSeconds) : 0;
        long minutesUsedToday = secondsUsed / 60;

        Optional<ParentChildCustomLimitEntity> customOpt =
                customLimitRepository.findByParentIdAndChildId(parentId, childId);

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
            return new SessionStatusDto("BLOCKED", "Время сессии исчерпано.", 0);
        }

        return new SessionStatusDto("ACTIVE", "Доступ разрешен", maxMinutes - minutesUsedToday);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentAgeLimitResponse> getLimits() {
        Long parentId = AuthenticationHolder.getUserId();
        return limitMapper.toListDto(limitRepository.findAllByParentId(parentId));
    }

    @Override
    @Transactional
    public void saveLimit(int age, int maxMinutes) {
        Long parentId = AuthenticationHolder.getUserId();
        ParentAgeLimitEntity limit = limitRepository.findByParentIdAndAge(parentId, age)
                .orElse(new ParentAgeLimitEntity(null, parentId, age, maxMinutes));
        limit.setMaxMinutes(maxMinutes);
        limitRepository.save(limit);
    }

    @Override
    @Transactional
    public void saveCustomLimit(Long childId, int minutes) {
        Long parentId = AuthenticationHolder.getUserId();
        ParentChildCustomLimitEntity limit = customLimitRepository
                .findByParentIdAndChildId(parentId, childId)
                .orElse(minutes == 0 ? null : new ParentChildCustomLimitEntity(null, parentId, childId, minutes));

        if (limit != null) {
            if (minutes == 0) {
                customLimitRepository.delete(limit);
            } else {
                limit.setCustomMinutes(minutes);
                customLimitRepository.save(limit);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentAgeLimitCustomResponse> getCustomLimits() {
        return limitMapper.toListCustomDto(
                customLimitRepository.findAllByParentId(AuthenticationHolder.getUserId())
        );
    }
}
