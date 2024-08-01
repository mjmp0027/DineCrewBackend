package com.dinecrew.dinecrewbackend.usuarios;

import com.dinecrew.dinecrewbackend.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private Role role;

    public static UserDto fromDocument(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
