package com.sa.notifications.notification.application.unsuscribeemployeeusecase;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import com.sa.notifications.notification.infrastructure.outputadapters.restapi.NotificationRestApiOutputAdapter;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UnsuscribeEmployeeUseCaseTest {
    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Mock
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;

    @Mock
    private NotificationRestApiOutputAdapter notificationRestApiOutputAdapter;

    @InjectMocks
    private UnsuscribeEmployeeUseCase unsuscribeEmployeeUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnsuscribeEmployee_Success() {
        // Datos simulados
        String typeNotification = "Hiring";
        String emailEmployee = "employee@example.com";

        // Simular una notificación existente
        Notification mockNotification = Notification.builder()
                .id(UUID.randomUUID())
                .type(typeNotification)
                .build();

        // Simular un empleado suscrito
        EmployeeNotification mockEmployeeNotification = EmployeeNotification.builder()
                .id(UUID.randomUUID())
                .emailEmployee(emailEmployee)
                .notification(mockNotification)
                .build();

        // Simular respuestas de los métodos mock
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(mockNotification);
        when(employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(mockNotification, emailEmployee))
                .thenReturn(mockEmployeeNotification);
        when(notificationRestApiOutputAdapter.checkEmailEmployee(emailEmployee)).thenReturn(true);

        // Ejecutar el caso de uso
        unsuscribeEmployeeUseCase.unsuscribeEmployee(typeNotification, emailEmployee);

        // Verificar que se desuscribió correctamente al empleado
        verify(employeeNotificationDbOutputAdapter, times(1)).unsuscribeEmployee(mockEmployeeNotification);
    }

    @Test
    void testUnsuscribeEmployee_NotificationDoesNotExist() {
        String typeNotification = "InvalidType";
        String emailEmployee = "employee@example.com";

        // Simular que la notificación no existe
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(null);
        when(notificationRestApiOutputAdapter.checkEmailEmployee(emailEmployee)).thenReturn(true);

        // Verificar que se lanza una excepción si la notificación no existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unsuscribeEmployeeUseCase.unsuscribeEmployee(typeNotification, emailEmployee);
        });

        assertEquals("La notificacion tiene que existir para ser unsuscrita", exception.getMessage());

        // Verificar que no se realizaron otras interacciones
        verify(employeeNotificationDbOutputAdapter, never()).FindEmployeeNotificationByEmailAndEmployee(any(), any());
        verify(employeeNotificationDbOutputAdapter, never()).unsuscribeEmployee(any());
    }

    @Test
    void testUnsuscribeEmployee_EmployeeNotSubscribed() {
        String typeNotification = "Hiring";
        String emailEmployee = "nonexistent@example.com";

        // Simular una notificación existente
        Notification mockNotification = Notification.builder()
                .id(UUID.randomUUID())
                .type(typeNotification)
                .build();

        // Simular que el empleado no está suscrito
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(mockNotification);
        when(employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(mockNotification, emailEmployee))
                .thenReturn(null);
        when(notificationRestApiOutputAdapter.checkEmailEmployee(emailEmployee)).thenReturn(true);

        // Verificar que se lanza una excepción si el empleado no está suscrito
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unsuscribeEmployeeUseCase.unsuscribeEmployee(typeNotification, emailEmployee);
        });

        assertEquals("El empleado tiene que estar suscrito a la notificacion de tipo: " + typeNotification, exception.getMessage());

        // Verificar que no se realizaron otras interacciones
        verify(employeeNotificationDbOutputAdapter, never()).unsuscribeEmployee(any());
    }

    @Test
    void testUnsuscribeEmployee_InvalidEmail() {
        String typeNotification = "Hiring";
        String emailEmployee = "invalid@example.com";

        // Simular que el email es inválido
        when(notificationRestApiOutputAdapter.checkEmailEmployee(emailEmployee)).thenReturn(false);

        // Verificar que se lanza una excepción si el email del empleado es inválido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unsuscribeEmployeeUseCase.unsuscribeEmployee(typeNotification, emailEmployee);
        });

        assertEquals("El email del empleado es incorrecto o no devuelve informacion", exception.getMessage());

        // Verificar que no se realizaron otras interacciones
        verify(notificationDbOutputAdapter, never()).findNotificationByType(anyString());
        verify(employeeNotificationDbOutputAdapter, never()).FindEmployeeNotificationByEmailAndEmployee(any(), any());
        verify(employeeNotificationDbOutputAdapter, never()).unsuscribeEmployee(any());
    }

    @Test
    void testUnsuscribeEmployee_InvalidNotificationType() {
        String typeNotification = "";
        String emailEmployee = "employee@example.com";

        // Verificar que se lanza una excepción si el tipo de notificación es inválido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unsuscribeEmployeeUseCase.unsuscribeEmployee(typeNotification, emailEmployee);
        });

        assertEquals("Tiene que haber un tipo de notificacion para agregarla", exception.getMessage());

        // Verificar que no se realizaron otras interacciones
        verify(notificationDbOutputAdapter, never()).findNotificationByType(anyString());
        verify(employeeNotificationDbOutputAdapter, never()).FindEmployeeNotificationByEmailAndEmployee(any(), any());
        verify(employeeNotificationDbOutputAdapter, never()).unsuscribeEmployee(any());
    }
}
