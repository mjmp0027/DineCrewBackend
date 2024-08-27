package com.dinecrew.dinecrewbackend.notificaciones;

import com.dinecrew.dinecrewbackend.enums.Estado;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "notificaciones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    private String id;
    private String titulo;
    private String mensaje;
    private LocalDateTime timestamp;
    private String userId;
    private Estado leida;
    private List<String> items;
}
