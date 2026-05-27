package com.umograd.analytic.service.impl;

import com.umograd.analytic.dto.AccessResponse;
import com.umograd.analytic.entity.UserEntity;
import com.umograd.analytic.repository.analytic.UserAnalyticsRepository;
import com.umograd.analytic.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAccessControlService implements AccessControlService {

    private final UserAnalyticsRepository userRepository;

    public AccessResponse checkAccess(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    boolean isUnderage = user.getAge() < 16;
                    boolean noConsent = !user.isParentConsent();
                    boolean hasActiveSubscription = userRepository.findById(user.getParentId())
                            .map(UserEntity::isHasActiveSubscription)
                            .orElse(false);
                    if (isUnderage && noConsent) {
                        return new AccessResponse(false,
                                "Пользователям младше 16 лет требуется согласие родителя.");
                    }
                    if (!hasActiveSubscription) {
                        return new AccessResponse(false,
                                "Для доступа к заданиям требуется активная подписка.");
                    }
                    return new AccessResponse(true, "Доступ разрешен");
                })
                .orElse(new AccessResponse(false, "Пользователь не найден"));
    }
}
