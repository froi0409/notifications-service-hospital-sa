package com.sa.notifications.employeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeNotificationDbEntityTest {

    private EmployeeNotification employeeNotification;
    private NotificationDbEntity notificationDbEntity;
    private EmployeeNotificationDbEntity employeeNotificationDbEntity;

    @BeforeEach
    void setUp() {
        // Configuración de prueba para NotificationDbEntity
        notificationDbEntity = new NotificationDbEntity();
        notificationDbEntity.setId(UUID.randomUUID().toString());
        notificationDbEntity.setType("Test Notification");

        // Configuración de prueba para EmployeeNotification
        employeeNotification = EmployeeNotification.builder()
                .id(UUID.randomUUID())
                .notification(Notification.builder()
                        .id(UUID.fromString(notificationDbEntity.getId()))
                        .type(notificationDbEntity.getType())
                        .build())
                .emailEmployee("test@example.com")
                .build();

        // Instancia para la clase EmployeeNotificationDbEntity
        employeeNotificationDbEntity = new EmployeeNotificationDbEntity();
        employeeNotificationDbEntity.setId(UUID.randomUUID().toString());
        employeeNotificationDbEntity.setNotification(notificationDbEntity);
        employeeNotificationDbEntity.setEmailEmployee("test@example.com");
    }

    @Test
    void testToDomainModel() {
        // Conversión de EmployeeNotificationDbEntity a EmployeeNotification
        EmployeeNotification domainModel = employeeNotificationDbEntity.toDomainModel();

        // Verificaciones
        assertNotNull(domainModel);
        assertEquals(employeeNotificationDbEntity.getId(), domainModel.getId().toString());
        assertEquals(employeeNotificationDbEntity.getNotification().toDomainModel().getId(), domainModel.getNotification().getId());
        assertEquals(employeeNotificationDbEntity.getEmailEmployee(), domainModel.getEmailEmployee());
    }

    @Test
    void testFromDomainModel() {
        // Conversión desde el modelo de dominio a EmployeeNotificationDbEntity
        EmployeeNotificationDbEntity entity = EmployeeNotificationDbEntity.from(employeeNotification);

        // Verificaciones
        assertNotNull(entity);
        assertEquals(employeeNotification.getId().toString(), entity.getId());
        assertEquals(employeeNotification.getNotification().getId().toString(), entity.getNotification().getId());
        assertEquals(employeeNotification.getEmailEmployee(), entity.getEmailEmployee());
    }

    @Test
    void testFromDomainModelWithNullId() {
        // EmployeeNotification sin id
        EmployeeNotification employeeWithoutId = EmployeeNotification.builder()
                .notification(employeeNotification.getNotification())
                .emailEmployee("test@example.com")
                .build();

        // Conversión desde el modelo de dominio a EmployeeNotificationDbEntity
        EmployeeNotificationDbEntity entity = EmployeeNotificationDbEntity.from(employeeWithoutId);

        // Verificaciones
        assertNotNull(entity.getId()); // Debe generar un UUID aleatorio
        assertEquals(employeeWithoutId.getNotification().getId().toString(), entity.getNotification().getId());
        assertEquals(employeeWithoutId.getEmailEmployee(), entity.getEmailEmployee());
    }

    @Test
    void testToDomainModelWithNullNotification() {
        // Si el notification es null, deberíamos recibir un NullPointerException
        EmployeeNotificationDbEntity entity = new EmployeeNotificationDbEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setEmailEmployee("test@example.com");

        assertThrows(NullPointerException.class, entity::toDomainModel);
    }
}
