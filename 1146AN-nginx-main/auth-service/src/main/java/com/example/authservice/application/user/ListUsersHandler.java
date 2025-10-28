package com.example.authservice.application.user;

import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.interfaces.rest.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListUsersHandler {
    private final UserRepository userRepository;

    public ListUsersHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserResponse> handle(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);

        return page.map(user -> new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getRole().getValue().name()
        ));
    }
}
