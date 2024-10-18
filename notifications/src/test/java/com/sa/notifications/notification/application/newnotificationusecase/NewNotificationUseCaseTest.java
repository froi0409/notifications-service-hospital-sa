package com.sa.notifications.notification.application.newnotificationusecase;

import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NewNotificationUseCaseTest {
    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @InjectMocks
    private NewNotificationUseCase newNotificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNewNotification_Success() {
        // Datos simulados
        String typeNotification = "Hiring";

        // Simular que no existe una notificación con el mismo tipo
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(null);

        // Ejecutar el caso de uso
        newNotificationUseCase.newNotification(typeNotification);

        // Verificar que se crea la nueva notificación
        verify(notificationDbOutputAdapter, times(1)).newNotification(any(Notification.class));
    }

    @Test
    void testNewNotification_NotificationAlreadyExists() {
        String typeNotification = "Hiring";

        // Simular que ya existe una notificación con el mismo tipo
        Notification existingNotification = Notification.builder()
                .type(typeNotification)
                .build();
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(existingNotification);

        // Verificar que se lanza una excepción si la notificación ya existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            newNotificationUseCase.newNotification(typeNotification);
        });

        assertEquals("La notificacion tiene que ser nueva para poder agregarla", exception.getMessage());

        // Verificar que no se llama al método para crear una nueva notificación
        verify(notificationDbOutputAdapter, never()).newNotification(any(Notification.class));
    }

    @Test
    void testNewNotification_InvalidNotificationType() {
        String invalidType = "";

        // Verificar que se lanza una excepción si el tipo de notificación es inválido (vacío o nulo)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            newNotificationUseCase.newNotification(invalidType);
        });

        assertEquals("Tiene que haber un tipo de notificacion para agregarla", exception.getMessage());

        // Verificar que no se llama al método para crear una nueva notificación
        verify(notificationDbOutputAdapter, never()).newNotification(any(Notification.class));
    }

    @Test
    void testNewNotification_NullNotificationType() {
        String nullType = null;

        // Verificar que se lanza una excepción si el tipo de notificación es nulo
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            newNotificationUseCase.newNotification(nullType);
        });

        assertEquals("Tiene que haber un tipo de notificacion para agregarla", exception.getMessage());

        // Verificar que no se llama al método para crear una nueva notificación
        verify(notificationDbOutputAdapter, never()).newNotification(any(Notification.class));
    }
}
