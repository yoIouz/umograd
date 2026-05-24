package com.umograd.application.user.query;

import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ToggleConsentQueryHandler {

    private final UserRepository userRepository;

    @Transactional
    public void toggleConsent(Long childId, boolean consent) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        child.setParentConsent(consent);
        userRepository.save(child);
    }
}
