package com.sa.notifications.notification.infrastructure.outputadapters.db;

import com.sa.notifications.notification.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationDbEntityTest {
 private NotificationDbEntity notificationDbEntity;
    private Notification notificationDomain;

    @BeforeEach
    void setUp() {
        // Crear un objeto NotificationDbEntity de ejemplo
        notificationDbEntity = new NotificationDbEntity();
        notificationDbEntity.setId(UUID.randomUUID().toString());
        notificationDbEntity.setType("Email");

        // Crear un objeto de dominio Notification de ejemplo
        notificationDomain = Notification.builder()
                .id(UUID.randomUUID())
                .type("SMS")
                .build();
    }

    @Test
    void testToDomainModel() {
        // Convertir la entidad a modelo de dominio
        Notification domainModel = notificationDbEntity.toDomainModel();

        // Verificar que los valores sean los correctos
        assertEquals(UUID.fromString(notificationDbEntity.getId()), domainModel.getId());
        assertEquals(notificationDbEntity.getType(), domainModel.getType());
    }

    @Test
    void testFromDomainModel() {
        // Convertir el modelo de dominio a entidad
        NotificationDbEntity entity = NotificationDbEntity.from(notificationDomain);

        // Verificar que los valores sean los correctos
        assertEquals(notificationDomain.getId().toString(), entity.getId());
        assertEquals(notificationDomain.getType(), entity.getType());
    }

    @Test
    void testFromDomainModelGeneratesIdIfNull() {
        // Crear una notificación de dominio sin ID
        Notification notificationWithoutId = Notification.builder()
                .type("Push")
                .build();

        // Convertir a entidad
        NotificationDbEntity entity = NotificationDbEntity.from(notificationWithoutId);

        // Verificar que el ID se genera
        assertNotNull(entity.getId());
        assertEquals("Push", entity.getType());
    }
}
