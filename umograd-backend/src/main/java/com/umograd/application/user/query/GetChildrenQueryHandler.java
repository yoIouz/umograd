package com.umograd.application.user.query;

import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChildrenQueryHandler {

    private final UserRepository userRepository;

    public List<User> handle(GetChildrenQuery query) {
        User parent = userRepository.findById(query.parentId())
                .orElseThrow(() -> new RuntimeException("Родитель не найден"));
        return userRepository.findByParent(parent);
    }
}
