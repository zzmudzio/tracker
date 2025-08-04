package com.tracker.tracker.service;

import com.tracker.tracker.entity.User;
import com.tracker.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }
}
