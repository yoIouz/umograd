package com.umograd.application.user.command;

import com.umograd.domain.user.Role;
import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserRoleCommandHandler {

    private final UserRepository userRepository;

    public User handle(UpdateUserRoleCommand command) {
        if (command.newRole() == null) {
            throw new IllegalArgumentException("Роль не может быть пустой");
        }
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isOnlyModerator = userRepository.countByRolesContaining(Role.ROLE_MODERATOR) == 1;
        if (isOnlyModerator && user.getRoles().contains(Role.ROLE_MODERATOR) && command.newRole() != Role.ROLE_MODERATOR) {
            throw new RuntimeException("Нельзя понизить роль единственного модератора");
        }

        // ✅ вместо Set.of(...) — очищаем и добавляем новую роль
        user.getRoles().clear();
        user.getRoles().add(command.newRole());

        return userRepository.save(user);
    }
}
