package com.dinecrew.dinecrewbackend.config;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final String token;
    private final String userId;

    public JwtResponse(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
}
