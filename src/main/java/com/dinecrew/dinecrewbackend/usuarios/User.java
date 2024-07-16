package com.dinecrew.dinecrewbackend.usuarios;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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
    private Date resetPasswordExpires;
}
