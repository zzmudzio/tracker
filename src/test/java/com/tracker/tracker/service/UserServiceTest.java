package com.tracker.tracker.service;


import com.tracker.tracker.entity.User;
import com.tracker.tracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void shouldReturnUserByLogin() {
        User user = User.builder()
                .login("test_login")
                .id(1)
                .password("test_password").build();

        when(userRepository.findUserByLogin(any(String.class))).thenReturn(Optional.of(user));

        assertEquals(Optional.of(user), userService.findUserByLogin("test"));
    }

    @Test
    public void shouldReturnEmptyOptional() {

        when(userRepository.findUserByLogin(any(String.class))).thenReturn(Optional.empty());

        assertTrue(userService.findUserByLogin("test").isEmpty());

    }
}
