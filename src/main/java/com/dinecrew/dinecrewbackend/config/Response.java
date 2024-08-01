package com.dinecrew.dinecrewbackend.config;

import lombok.Getter;

import java.util.Map;

@Getter
public class Response {

    private final Map<String, String> message;

    public Response(String message) {
        this.message = Map.of("message", message);
    }

}
