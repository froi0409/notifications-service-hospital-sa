package com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbEntity;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeNotificationLogDbEntityTest {

    private EmployeeNotificationLogDbEntity entity;
    private NotificationLogDbEntity notificationLogDbEntityMock;

    @BeforeEach
    void setUp() {
        notificationLogDbEntityMock = mock(NotificationLogDbEntity.class);
        entity = new EmployeeNotificationLogDbEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setNotification(notificationLogDbEntityMock);
        entity.setEmailEmployee("test@example.com");
    }

    @Test
    void testToDomainModel() {
        // Given
        when(notificationLogDbEntityMock.toDomainModel()).thenReturn(NotificationLog.builder()
                .id(UUID.randomUUID())
                .description("Test description")
                .date(LocalDateTime.now())
                .build());
        
        // When
        EmployeeNotificationLog domainModel = entity.toDomainModel();
        
        // Then
        assertNotNull(domainModel);
        assertEquals(UUID.fromString(entity.getId()), domainModel.getId());
        assertEquals(entity.getEmailEmployee(), domainModel.getEmailEmployee());
        assertNotNull(domainModel.getNotification());
    }

    @Test
void testFrom() {
    // Given
    Notification notification = Notification.builder()
            .id(UUID.randomUUID())
            .build();

    NotificationLog notificationLog = NotificationLog.builder()
            .id(UUID.randomUUID())
            .notification(notification)
            .description("Test notification")
            .date(LocalDateTime.now())
            .build();

    EmployeeNotificationLog domainModel = EmployeeNotificationLog.builder()
            .id(UUID.randomUUID())
            .notification(notificationLog)
            .emailEmployee("test@example.com")
            .build();

    // Crea una instancia real de NotificationLogDbEntity sin simular el método estático
    NotificationLogDbEntity notificationLogDbEntity = new NotificationLogDbEntity();
    notificationLogDbEntity.setId(notificationLog.getId().toString());
    notificationLogDbEntity.setDescription(notificationLog.getDescription());
    notificationLogDbEntity.setDate(notificationLog.getDate());
    notificationLogDbEntity.setNotification(new NotificationDbEntity()); // Configura según lo que necesites

    // When
    EmployeeNotificationLogDbEntity result = EmployeeNotificationLogDbEntity.from(domainModel);

    // Then
    assertNotNull(result);
    assertEquals(domainModel.getId().toString(), result.getId());
    assertEquals(domainModel.getEmailEmployee(), result.getEmailEmployee());
    assertNotNull(result.getNotification());
}

}
