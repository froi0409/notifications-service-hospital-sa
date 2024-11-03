package com.sa.notifications.notification.application.getallsuscribersusecase;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllSuscribersUseCaseTest {

    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Mock
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;

    @InjectMocks
    private GetAllSuscribersUseCase getAllSuscribersUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSuscribers_Success() {
        // Datos simulados
        String type = "testType";
        Notification mockNotification = Notification.builder()
                .type(type)
                .build();
        
        List<EmployeeNotification> mockEmployeeNotifications = new ArrayList<>();
        mockEmployeeNotifications.add(EmployeeNotification.builder().emailEmployee("employee1@example.com").build());
        mockEmployeeNotifications.add(EmployeeNotification.builder().emailEmployee("employee2@example.com").build());

        // Simular comportamiento de los adaptadores
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(mockNotification);
        when(employeeNotificationDbOutputAdapter.findEmployeeNotificationByNotificationId(mockNotification)).thenReturn(mockEmployeeNotifications);

        // Ejecutar el caso de uso
        List<EmployeeNotification> result = getAllSuscribersUseCase.getAllSuscribers(type);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("employee1@example.com", result.get(0).getEmailEmployee());
        assertEquals("employee2@example.com", result.get(1).getEmailEmployee());

        // Verificar interacciones con los adaptadores
        verify(notificationDbOutputAdapter, times(1)).findNotificationByType(type);
        verify(employeeNotificationDbOutputAdapter, times(1)).findEmployeeNotificationByNotificationId(mockNotification);
    }

    @Test
    void testGetAllSuscribers_NotificationNotFound() {
        // Datos simulados
        String type = "nonExistentType";

        // Simular que no se encuentra la notificación
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(null);

        // Ejecutar el caso de uso y esperar una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getAllSuscribersUseCase.getAllSuscribers(type);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Tiene que haber un tipo de notificacion para buscar suscriptores", exception.getMessage());

        // Verificar interacciones con los adaptadores
        verify(notificationDbOutputAdapter, times(1)).findNotificationByType(type);
        verify(employeeNotificationDbOutputAdapter, times(0)).findEmployeeNotificationByNotificationId(any(Notification.class));
    }

    @Test
    void testGetAllSuscribers_NoSubscribersFound() {
        // Datos simulados
        String type = "testType";
        Notification mockNotification = Notification.builder()
                .type(type)
                .build();

        // Simular comportamiento de los adaptadores
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(mockNotification);
        when(employeeNotificationDbOutputAdapter.findEmployeeNotificationByNotificationId(mockNotification)).thenReturn(new ArrayList<>());

        // Ejecutar el caso de uso
        List<EmployeeNotification> result = getAllSuscribersUseCase.getAllSuscribers(type);

        // Verificar que la lista esté vacía
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verificar interacciones con los adaptadores
        verify(notificationDbOutputAdapter, times(1)).findNotificationByType(type);
        verify(employeeNotificationDbOutputAdapter, times(1)).findEmployeeNotificationByNotificationId(mockNotification);
    }
}