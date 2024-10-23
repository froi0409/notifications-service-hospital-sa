package com.sa.notifications.notification.application.updatenotificationusecase;

import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateNotificationUseCaseTest {
    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @InjectMocks
    private UpdateNotificationUseCase updateNotificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateNotification_Success() {
        // Datos simulados
        String oldType = "Hiring";
        String newType = "Promotion";

        // Simular que existe una notificación con el tipo antiguo
        Notification existingNotification = Notification.builder().type(oldType).build();
        when(notificationDbOutputAdapter.findNotificationByType(oldType)).thenReturn(existingNotification);

        // Simular que no existe una notificación con el nuevo tipo
        when(notificationDbOutputAdapter.findNotificationByType(newType)).thenReturn(null);

        // Ejecutar el caso de uso
        updateNotificationUseCase.updateNotification(newType, oldType);

        // Verificar que se actualiza la notificación correctamente
        assertEquals(newType, existingNotification.getType());
        verify(notificationDbOutputAdapter, times(1)).updateNotification(existingNotification);
    }

    @Test
    void testUpdateNotification_OldNotificationDoesNotExist() {
        String oldType = "Hiring";
        String newType = "Promotion";

        // Simular que no existe una notificación con el tipo antiguo
        when(notificationDbOutputAdapter.findNotificationByType(oldType)).thenReturn(null);

        // Verificar que se lanza una excepción si la notificación antigua no existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            updateNotificationUseCase.updateNotification(newType, oldType);
        });

        assertEquals("La notificacion de tipo" + oldType + " tiene que existir para poder actualizarla", exception.getMessage());

        // Verificar que no se actualiza ninguna notificación
        verify(notificationDbOutputAdapter, never()).updateNotification(any(Notification.class));
    }

    @Test
    void testUpdateNotification_NewNotificationTypeAlreadyExists() {
        String oldType = "Hiring";
        String newType = "Promotion";

        // Simular que existe una notificación con el tipo antiguo
        Notification existingNotification = Notification.builder().type(oldType).build();
        when(notificationDbOutputAdapter.findNotificationByType(oldType)).thenReturn(existingNotification);

        // Simular que ya existe una notificación con el nuevo tipo
        Notification newTypeNotification = Notification.builder().type(newType).build();
        when(notificationDbOutputAdapter.findNotificationByType(newType)).thenReturn(newTypeNotification);

        // Verificar que se lanza una excepción si el nuevo tipo de notificación ya existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            updateNotificationUseCase.updateNotification(newType, oldType);
        });

        assertEquals("La notificacion de tipo " + newType + " tiene que ser nueva para poder agregarla", exception.getMessage());

        // Verificar que no se actualiza ninguna notificación
        verify(notificationDbOutputAdapter, never()).updateNotification(any(Notification.class));
    }

    @Test
    void testUpdateNotification_InvalidOldNotificationType() {
        String oldType = ""; // Tipo antiguo inválido (vacío)
        String newType = "Promotion";

        // Verificar que se lanza una excepción si el tipo antiguo es inválido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            updateNotificationUseCase.updateNotification(newType, oldType);
        });

        assertEquals("Tiene que haber un tipo antiguo de notificacion para modificarla", exception.getMessage());

        // Verificar que no se llama al método de actualización de notificación
        verify(notificationDbOutputAdapter, never()).updateNotification(any(Notification.class));
    }

    @Test
    void testUpdateNotification_InvalidNewNotificationType() {
        String oldType = "Hiring";
        String newType = ""; // Tipo nuevo inválido (vacío)

        // Simular que existe una notificación con el tipo antiguo
        Notification existingNotification = Notification.builder().type(oldType).build();
        when(notificationDbOutputAdapter.findNotificationByType(oldType)).thenReturn(existingNotification);

        // Verificar que se lanza una excepción si el nuevo tipo es inválido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            updateNotificationUseCase.updateNotification(newType, oldType);
        });

        assertEquals("Tiene que haber un tipo nuevo de notificacion para modificarla", exception.getMessage());

        // Verificar que no se actualiza ninguna notificación
        verify(notificationDbOutputAdapter, never()).updateNotification(any(Notification.class));
    }
}
