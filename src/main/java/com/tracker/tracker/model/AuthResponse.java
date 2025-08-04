package com.tracker.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private String type;
}
