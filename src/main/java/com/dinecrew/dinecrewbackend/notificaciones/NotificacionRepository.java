package com.dinecrew.dinecrewbackend.notificaciones;

import com.dinecrew.dinecrewbackend.enums.Estado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends MongoRepository<Notificacion, String> {
    @Query("{userId:  ?0, 'timestamp':  { $gte:  ?1 }, 'leida':  ?2 }")
    List<Notificacion> findRecentNotificationsNoReads(String userId, LocalDateTime timestamp, Estado leida);

    @Query("{userId:  ?0, 'timestamp':  { $gte:  ?1 }}")
    List<Notificacion> findRecentNotificationsByUserId(String userId, LocalDateTime timestamp);
}
