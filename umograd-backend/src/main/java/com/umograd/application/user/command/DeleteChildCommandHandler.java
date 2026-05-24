package com.umograd.application.user.command;

import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteChildCommandHandler {

    private final UserRepository userRepository;

    public void handle(DeleteChildCommand command) {
        User child = userRepository.findById(command.childId())
                .orElseThrow(() -> new RuntimeException("Ребёнок не найден"));

        if (child.getParent() == null || !child.getParent().getId().equals(command.parentId())) {
            throw new RuntimeException("Нет прав на удаление этого ребёнка");
        }

        userRepository.delete(child);
    }
}
