package com.umograd.application.user.command;

import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserCommandHandler {

    private final UserRepository userRepository;

    public void handle(DeleteUserCommand command) {
        userRepository.deleteById(command.userId());
    }
}
