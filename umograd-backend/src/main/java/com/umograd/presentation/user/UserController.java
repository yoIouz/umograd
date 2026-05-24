package com.umograd.presentation.user;

import com.umograd.application.user.command.UpdateUserProfileCommand;
import com.umograd.application.user.command.UpdateUserProfileHandler;
import com.umograd.application.user.command.UpdateUserProfileResponse;
import com.umograd.presentation.user.dto.UserProfileDto;
import com.umograd.application.user.query.GetUserProfileHandler;
import com.umograd.application.user.query.GetUserProfileQuery;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final GetUserProfileHandler getHandler;
    private final UpdateUserProfileHandler updateHandler;

    public UserController(GetUserProfileHandler getHandler, UpdateUserProfileHandler updateHandler) {
        this.getHandler = getHandler;
        this.updateHandler = updateHandler;
    }

    @GetMapping("/me")
    public UserProfileDto getProfile(Authentication auth) {
        return getHandler.handle(new GetUserProfileQuery(auth.getName()));
    }

    @PutMapping("/me")
    public UpdateUserProfileResponse updateProfile(Authentication auth, @RequestBody UserProfileDto dto) {
        return updateHandler.handle(new UpdateUserProfileCommand(
                auth.getName(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getBirthDate(),
                dto.getAvatarUrl()
        ));
    }
}
