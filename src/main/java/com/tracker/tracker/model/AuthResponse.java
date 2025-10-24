package com.tracker.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Date;

@AllArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private String type;
    private long expirationDateMilli;
    private ZonedDateTime expirationDate;
}
