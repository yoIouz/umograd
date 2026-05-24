package com.umograd.presentation.common.mapper;

import com.umograd.domain.user.User;
import com.umograd.presentation.moderator.dto.UserResponse;
import com.umograd.presentation.parent.dto.ChildResponse;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );
    }

    public static ChildResponse toChildResponse(User user) {
        return new ChildResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isParentConsent()
        );
    }
}
