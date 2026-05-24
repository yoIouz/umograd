package com.umograd.application.user.command;

import com.umograd.presentation.user.dto.UserProfileDto;
import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import com.umograd.security.JwtTokenProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserProfileHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtService;
    private final UserDetailsService userDetailsService;

    public UpdateUserProfileHandler(UserRepository userRepository,
                                    JwtTokenProvider jwtService,
                                    UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public UpdateUserProfileResponse handle(UpdateUserProfileCommand command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setUsername(command.getNewUsername());
        user.setEmail(command.getNewEmail());
        user.setBirthDate(command.getNewBirthDate());
        user.setAvatarUrl(command.getNewAvatarUrl());

        userRepository.save(user);

        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Enum::name).toList());
        dto.setBirthDate(user.getBirthDate());
        dto.setAvatarUrl(user.getAvatarUrl());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getUsername(),
                user.getBirthDate(),
                user.getEmail(),
                userDetails
        );
        String refreshToken = jwtService.generateRefreshToken(
                user.getId(),
                user.getUsername(),
                user.getBirthDate(),
                user.getEmail(),
                userDetails
        );

        return new UpdateUserProfileResponse(dto, accessToken, refreshToken);
    }
}
