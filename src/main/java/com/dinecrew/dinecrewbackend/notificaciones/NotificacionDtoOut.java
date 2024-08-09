package com.dinecrew.dinecrewbackend.notificaciones;

import com.dinecrew.dinecrewbackend.enums.Estado;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class NotificacionDtoOut {

    private String id;
    private String titulo;
    private String mensaje;
    private LocalDateTime timestamp;
    private String userId;
    private Estado leida;

    public static NotificacionDtoOut fromDocument(Notificacion notificacion) {
        return NotificacionDtoOut.builder()
                .id(notificacion.getId())
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .timestamp(notificacion.getTimestamp())
                .userId(notificacion.getUserId())
                .leida(notificacion.getLeida())
                .build();
    }

    public static List<NotificacionDtoOut> fromDocuments(List<Notificacion> notificaciones) {
        return notificaciones.stream()
                .map(NotificacionDtoOut::fromDocument)
                .collect(Collectors.toList());
    }
}
