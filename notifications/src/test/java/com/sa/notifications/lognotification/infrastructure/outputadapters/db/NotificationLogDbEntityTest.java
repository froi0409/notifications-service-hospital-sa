package com.sa.notifications.lognotification.infrastructure.outputadapters.db;

import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationLogDbEntityTest {

    private NotificationLog notificationLog;
    private NotificationDbEntity notificationDbEntity;
    private NotificationLogDbEntity notificationLogDbEntity;

    @BeforeEach
    void setUp() {
        // Configuración de prueba para NotificationDbEntity
        notificationDbEntity = new NotificationDbEntity();
        notificationDbEntity.setId(UUID.randomUUID().toString());
        notificationDbEntity.setType("Test Notification");

        // Configuración de prueba para NotificationLog
        notificationLog = NotificationLog.builder()
                .id(UUID.randomUUID())
                .notification(Notification.builder()
                        .id(UUID.fromString(notificationDbEntity.getId()))
                        .type(notificationDbEntity.getType())
                        .build())
                .description("Test Description")
                .date(LocalDateTime.now())
                .build();

        // Instancia para la clase NotificationLogDbEntity
        notificationLogDbEntity = new NotificationLogDbEntity();
        notificationLogDbEntity.setId(UUID.randomUUID().toString());
        notificationLogDbEntity.setNotification(notificationDbEntity);
        notificationLogDbEntity.setDescription("Test Description");
        notificationLogDbEntity.setDate(LocalDateTime.now());
    }

    @Test
    void testToDomainModel() {
        // Conversión de NotificationLogDbEntity a NotificationLog
        NotificationLog domainModel = notificationLogDbEntity.toDomainModel();

        // Verificaciones
        assertNotNull(domainModel);
        assertEquals(notificationLogDbEntity.getId(), domainModel.getId().toString());
        assertEquals(notificationLogDbEntity.getNotification().toDomainModel().getId(), domainModel.getNotification().getId());
        assertEquals(notificationLogDbEntity.getDescription(), domainModel.getDescription());
        assertEquals(notificationLogDbEntity.getDate(), domainModel.getDate());
    }

    @Test
    void testFromDomainModel() {
        // Conversión desde el modelo de dominio a NotificationLogDbEntity
        NotificationLogDbEntity entity = NotificationLogDbEntity.from(notificationLog);

        // Verificaciones
        assertNotNull(entity);
        assertEquals(notificationLog.getId().toString(), entity.getId());
        assertEquals(notificationLog.getNotification().getId().toString(), entity.getNotification().getId());
        assertEquals(notificationLog.getDescription(), entity.getDescription());
        assertEquals(notificationLog.getDate(), entity.getDate());
    }

    @Test
    void testFromDomainModelWithNullId() {
        // NotificationLog sin id
        NotificationLog logWithoutId = NotificationLog.builder()
                .notification(notificationLog.getNotification())
                .description("New Notification")
                .date(LocalDateTime.now())
                .build();

        // Conversión desde el modelo de dominio a NotificationLogDbEntity
        NotificationLogDbEntity entity = NotificationLogDbEntity.from(logWithoutId);

        // Verificaciones
        assertNotNull(entity.getId()); // Debe generar un UUID aleatorio
        assertEquals(logWithoutId.getNotification().getId().toString(), entity.getNotification().getId());
        assertEquals(logWithoutId.getDescription(), entity.getDescription());
        assertEquals(logWithoutId.getDate(), entity.getDate());
    }

    @Test
    void testToDomainModelWithNullNotification() {
        // Si el notification es null, deberíamos recibir un NullPointerException
        NotificationLogDbEntity entity = new NotificationLogDbEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDescription("Test Description");
        entity.setDate(LocalDateTime.now());

        assertThrows(NullPointerException.class, entity::toDomainModel);
    }
}
