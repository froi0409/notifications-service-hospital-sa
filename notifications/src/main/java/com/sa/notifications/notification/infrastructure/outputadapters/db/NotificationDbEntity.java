package com.sa.notifications.notification.infrastructure.outputadapters.db;

import com.sa.notifications.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Entity
@Table(name = "notification", schema = "notifications")
@NoArgsConstructor
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
        NotificationDbEntity entity = new NotificationDbEntity();
        entity.setId(notification.getId() != null ? notification.getId().toString() : UUID.randomUUID().toString());
        entity.setType(notification.getType());
        return entity;
    }
}