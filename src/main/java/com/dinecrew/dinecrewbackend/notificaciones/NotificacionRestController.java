package com.dinecrew.dinecrewbackend.notificaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionRestController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{usedId}")
    public ResponseEntity<?> obtenerNotificaciones(
            @PathVariable String usedId
    ) {

        List<Notificacion> notificaciones = notificationService.getNotificationsByUserId(usedId);
        return ResponseEntity.ok(NotificacionDtoOut.fromDocuments(notificaciones));
    }

    @PostMapping("/{usedId}")
    public ResponseEntity<?> marcarComoLeidas(
            @PathVariable String usedId
    ) {
        notificationService.marcarComoLeidas(usedId);
        return ResponseEntity.ok().build();
    }
}
