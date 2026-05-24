package com.umograd.application.user.command;

import com.umograd.presentation.user.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserProfileResponse {
    private UserProfileDto profile;
    private String accessToken;
    private String refreshToken;
}

