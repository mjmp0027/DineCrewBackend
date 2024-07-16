package com.dinecrew.dinecrewbackend.usuarios;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String username;
    private String email;
    private String password;

    public static UserDto fromDocument(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
