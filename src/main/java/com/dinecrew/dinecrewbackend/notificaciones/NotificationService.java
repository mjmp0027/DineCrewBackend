package com.dinecrew.dinecrewbackend.notificaciones;

import com.dinecrew.dinecrewbackend.enums.Estado;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificacionRepository repository;


    public List<Notificacion> getNotificationsByUserId(String userId) {
        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        return repository.findRecentNotificationsByUserId(userId, threeHoursAgo);
    }

    public void save(Notificacion pedidoListo) {
        repository.save(pedidoListo);
    }

    public void marcarComoLeidas(String userId) {
        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        List<Notificacion> notificaciones = repository.findRecentNotificationsNoReads(userId, threeHoursAgo, Estado.NOLEIDA);
        notificaciones.forEach(notificacion -> {
            notificacion.setLeida(Estado.LEIDA);
            repository.save(notificacion);
        });
    }

    public void sendOrderReadyNotification(String tableName, String username, List<String> items) {

        String itemsString = String.join(", ", items);
        Notification notification = Notification.builder()
                .setTitle("Pedido Listo")
                .setBody("El pedido de la mesa " + tableName + " está listo. Productos: " + itemsString)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setTopic(username)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
            System.out.println("Notification sent successfully");
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }
}
