package com.sa.notifications.notification.infrastructure.inputadapters.restapi;

import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputadapters.restapi.response.NotificationResponse;
import com.sa.notifications.notification.infrastructure.inputports.restapi.GetAllNotificationsInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.NewNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UpdateNotificationInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

public class NotificationControllerAdapterTest {
    
    @InjectMocks
    private NotificationControllerAdapter notificationControllerAdapter;

    @Mock
    private NewNotificationInputPort newNotificationInputPort;

    @Mock
    private UpdateNotificationInputPort updateNotificationInputPort;

    @Mock
    private GetAllNotificationsInputPort getAllNotificationsInputPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNewNotificationEmployee() {
        String notificationType = "Email";

        notificationControllerAdapter.newNotificationEmployee(notificationType);

        // Verificar que se llamó al método de crear notificación
        verify(newNotificationInputPort, times(1)).newNotification(notificationType);
    }

    @Test
    void testUpdateNotificationEmployee() {
        String oldType = "Email";
        String newType = "SMS";

        notificationControllerAdapter.newNotificationEmployee(oldType, newType);

        // Verificar que se llamó al método de actualizar notificación
        verify(updateNotificationInputPort, times(1)).updateNotification(newType, oldType);
    }

    @Test
    void testGetAllNotification() {
        // Simular la respuesta del servicio de obtención de notificaciones
        when(getAllNotificationsInputPort.getAllNotification()).thenReturn(Collections.emptyList());

        List<NotificationResponse> response = notificationControllerAdapter.getAllNotification();

        // Verificar que se llamó al método de obtener notificaciones
        verify(getAllNotificationsInputPort, times(1)).getAllNotification();
        // Verificar que la respuesta sea una lista vacía
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetAllNotificationWithResponse() {
        // Crear una instancia simulada de notificación
        UUID notificationId = UUID.randomUUID();
        Notification mockNotification = Notification.builder()
                .id(notificationId)
                .type("Email")
                .build();
        
        List<Notification> mockNotifications = List.of(mockNotification);

        // Simular la respuesta del servicio de obtención de notificaciones
        when(getAllNotificationsInputPort.getAllNotification()).thenReturn(mockNotifications);

        List<NotificationResponse> response = notificationControllerAdapter.getAllNotification();

        // Verificar que se llamó al método de obtener notificaciones
        verify(getAllNotificationsInputPort, times(1)).getAllNotification();
        
        // Verificar que la respuesta no sea nula y tenga elementos
        assertNotNull(response);
        assertFalse(response.isEmpty());

        // Verificar que la respuesta tenga el tamaño esperado
        assertEquals(mockNotifications.size(), response.size());

        // Verificar que el contenido de la respuesta sea correcto
        for (int i = 0; i < mockNotifications.size(); i++) {
            assertEquals(mockNotifications.get(i).getId().toString(), response.get(i).getId());
            assertEquals(mockNotifications.get(i).getType(), response.get(i).getType());
        }
    }
}
