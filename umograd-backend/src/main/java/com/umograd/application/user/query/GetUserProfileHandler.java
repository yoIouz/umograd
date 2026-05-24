package com.umograd.application.user.query;

import com.umograd.presentation.user.dto.UserProfileDto;
import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class GetUserProfileHandler {

    private final UserRepository userRepository;

    public GetUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDto handle(GetUserProfileQuery query) {
        User user = userRepository.findByUsername(query.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Enum::name).toList());
        dto.setBirthDate(user.getBirthDate());
        dto.setAvatarUrl(user.getAvatarUrl());

        return dto;
    }
}
