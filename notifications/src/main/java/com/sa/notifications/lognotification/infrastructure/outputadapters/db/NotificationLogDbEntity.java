package com.sa.notifications.lognotification.infrastructure.outputadapters.db;

import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "notification_log", schema = "notifications")
public class NotificationLogDbEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_notification", nullable = false)
    private NotificationDbEntity notification;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    // Conversión a modelo de dominio
    public NotificationLog toDomainModel() {
        return NotificationLog.builder()
                .id(UUID.fromString(this.id))
                .notification(this.notification.toDomainModel())
                .description(this.description)
                .date(this.date)
                .build();
    }

    // Conversión desde el modelo de dominio
    public static NotificationLogDbEntity from(NotificationLog notificationLog) {
        return NotificationLogDbEntity.builder()
                .id(notificationLog.getId() != null ? notificationLog.getId().toString() : UUID.randomUUID().toString())
                .notification(NotificationDbEntity.from(notificationLog.getNotification()))
                .description(notificationLog.getDescription())
                .date(notificationLog.getDate() != null ? notificationLog.getDate() : LocalDateTime.now())
                .build();
    }
}