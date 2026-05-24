package com.umograd.application.user.query;

import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersQueryHandler {

    private final UserRepository userRepository;

    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}
