package com.sa.notifications.notification.infrastructure.outputadapters.db;

import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntity;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntityRepository;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationDbOutputAdapterTest {

    @Mock
    private NotificationDbEntityRepository notificationDbEntityRepository;

    @InjectMocks
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNewNotification_Success() {
        // Datos simulados
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .type("TestNotification")
                .build();

        NotificationDbEntity mockEntity = NotificationDbEntity.from(notification);

        // Simular comportamiento del repositorio
        when(notificationDbEntityRepository.save(any(NotificationDbEntity.class))).thenReturn(mockEntity);

        // Ejecutar el caso de uso
        Notification result = notificationDbOutputAdapter.newNotification(notification);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals("TestNotification", result.getType());

        // Verificar interacciones con el repositorio
        verify(notificationDbEntityRepository, times(1)).save(any(NotificationDbEntity.class));
    }

    @Test
    void testFindNotificationByType_Success() {
        // Datos simulados
        String type = "TestType";
        NotificationDbEntity mockEntity1 = new NotificationDbEntity();
        mockEntity1.setId(UUID.randomUUID().toString());
        mockEntity1.setType(type);

        // Simular comportamiento del repositorio
        when(notificationDbEntityRepository.findByType(type)).thenReturn(Arrays.asList(mockEntity1));

        // Ejecutar el caso de uso
        Notification result = notificationDbOutputAdapter.findNotificationByType(type);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(type, result.getType());

        // Verificar interacciones con el repositorio
        verify(notificationDbEntityRepository, times(1)).findByType(type);
    }

    @Test
    void testFindNotificationByType_NotFound() {
        // Datos simulados
        String type = "NonExistentType";

        // Simular que no se encuentra ninguna notificación
        when(notificationDbEntityRepository.findByType(type)).thenReturn(Arrays.asList());

        // Ejecutar el caso de uso
        Notification result = notificationDbOutputAdapter.findNotificationByType(type);

        // Verificar que el resultado sea nulo
        assertNull(result);

        // Verificar interacciones con el repositorio
        verify(notificationDbEntityRepository, times(1)).findByType(type);
    }

    @Test
    void testUpdateNotification_Success() {
        // Datos simulados
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .type("UpdatedNotification")
                .build();

        NotificationDbEntity mockEntity = NotificationDbEntity.from(notification);

        // Simular comportamiento del repositorio
        when(notificationDbEntityRepository.findById(notification.getId().toString())).thenReturn(Optional.of(mockEntity));
        when(notificationDbEntityRepository.save(any(NotificationDbEntity.class))).thenReturn(mockEntity);

        // Ejecutar el caso de uso
        Notification result = notificationDbOutputAdapter.updateNotification(notification);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals("UpdatedNotification", result.getType());

        // Verificar interacciones con el repositorio
        verify(notificationDbEntityRepository, times(1)).findById(notification.getId().toString());
        verify(notificationDbEntityRepository, times(1)).save(any(NotificationDbEntity.class));
    }

    @Test
    void testUpdateNotification_NotFound() {
        // Datos simulados
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .type("NonExistentNotification")
                .build();

        // Simular que la notificación no existe
        when(notificationDbEntityRepository.findById(notification.getId().toString())).thenReturn(Optional.empty());

        // Ejecutar el caso de uso
        Notification result = notificationDbOutputAdapter.updateNotification(notification);

        // Verificar que el resultado sea nulo
        assertNull(result);

        // Verificar interacciones con el repositorio
        verify(notificationDbEntityRepository, times(1)).findById(notification.getId().toString());
        verify(notificationDbEntityRepository, times(0)).save(any(NotificationDbEntity.class));
    }

    @Test
    void testGetAllNotifications_Success() {
        // Datos simulados
        NotificationDbEntity mockEntity1 = new NotificationDbEntity();
        mockEntity1.setId(UUID.randomUUID().toString());
        mockEntity1.setType("Notification1");

        NotificationDbEntity mockEntity2 = new NotificationDbEntity();
        mockEntity2.setId(UUID.randomUUID().toString());
        mockEntity2.setType("Notification2");

        List<NotificationDbEntity> mockEntities = Arrays.asList(mockEntity1, mockEntity2);

        // Simular comportamiento del repositorio
        when(notificationDbEntityRepository.findAll()).thenReturn(mockEntities);

        // Ejecutar el caso de uso
        List<Notification> result = notificationDbOutputAdapter.getAllNotifications();

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Notification1", result.get(0).getType());
        assertEquals("Notification2", result.get(1).getType());

        // Verificar interacciones con el repositorio
        verify(notificationDbEntityRepository, times(1)).findAll();
    }
}
