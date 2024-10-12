package com.sa.notifications.notification.infrastructure.outputadapters.db;

import com.sa.notifications.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "notification", schema = "notifications")
public class NotificationDbEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "type")
    private String type;

    // Conversión a modelo de dominio
    public Notification toDomainModel() {
        return Notification.builder()
                .id(UUID.fromString(this.getId()))
                .type(this.type)
                .build();
    }

    // Conversión desde el modelo de dominio
    public static NotificationDbEntity from(Notification notification) {
        return NotificationDbEntity.builder()
                .id(notification.getId() != null ? notification.getId().toString() : UUID.randomUUID().toString())
                .type(notification.getType())
                .build();
    }
}