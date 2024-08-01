package com.dinecrew.dinecrewbackend.usuarios;

import com.dinecrew.dinecrewbackend.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String resetPasswordToken;
    private List<String> mesasAsignadas; // Almacena los IDs de las mesas asignadas
    private Role role;
}
