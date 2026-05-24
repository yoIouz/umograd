package com.umograd.application.user.query;

import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByUsernameQueryHandler {

    private final UserRepository userRepository;

    public User handle(FindUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
