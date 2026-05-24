package com.umograd.application.user.command;

import lombok.Value;
import java.time.LocalDate;

@Value
public class UpdateUserProfileCommand {
    String username;
    String newUsername;
    String newEmail;
    LocalDate newBirthDate;
    String newAvatarUrl;
}