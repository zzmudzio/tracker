package com.tracker.tracker.controller;

import com.tracker.tracker.entity.User;
import com.tracker.tracker.model.AuthResponse;
import com.tracker.tracker.model.UserCredentials;
import com.tracker.tracker.security.JwtUtil;
import com.tracker.tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserCredentials userCredentials) {
        Optional<User> user = userService.findUserByLogin(userCredentials.getLogin());
        if (user.isPresent() && user.get().getPassword().equals(userCredentials.getPassword())) {
            String token = JwtUtil.generateToken(userCredentials.getLogin());
            return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect credentials!");
        }
    }
}
