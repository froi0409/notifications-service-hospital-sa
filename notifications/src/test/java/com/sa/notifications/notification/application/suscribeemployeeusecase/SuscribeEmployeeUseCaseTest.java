package com.sa.notifications.notification.application.suscribeemployeeusecase;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import com.sa.notifications.notification.infrastructure.outputadapters.restapi.NotificationRestApiOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SuscribeEmployeeUseCaseTest {
    
    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Mock
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;

    @Mock
    private NotificationRestApiOutputAdapter notificationRestApiOutputAdapter;

    @InjectMocks
    private SuscribeEmployeeUseCase suscribeEmployeeUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuscribeEmployee_Success() {
        // Datos simulados
        String type = "Promotion";
        String emailEmployee = "employee@example.com";

        // Simular que la notificación existe
        Notification notification = Notification.builder().type(type).build();
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(notification);

        // Simular que el empleado no está suscrito
        when(employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(notification, emailEmployee)).thenReturn(null);

        // Simular que el email del empleado es válido
        when(notificationRestApiOutputAdapter.checkEmailEmployee(emailEmployee)).thenReturn(true);

        // Ejecutar el caso de uso
        suscribeEmployeeUseCase.suscribeEmployee(type, emailEmployee);

        // Verificar que se crea una nueva suscripción
        verify(employeeNotificationDbOutputAdapter, times(1)).suscribeEmployee(any(EmployeeNotification.class));
    }
/*
    @Test
    void testSuscribeEmployee_NotificationDoesNotExist() {
        String type = "Promotion";
        String emailEmployee = "employee@example.com";

        // Simular que no existe una notificación con el tipo dado
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(null);

        // Verificar que se lanza una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            suscribeEmployeeUseCase.suscribeEmployee(type, emailEmployee);
        });

        assertEquals("La notificacion tiene que existir para ser agregada", exception.getMessage());

        // Verificar que no se intenta crear una nueva suscripción
        verify(employeeNotificationDbOutputAdapter, never()).suscribeEmployee(any(EmployeeNotification.class));
    }
*/
    /*
    @Test
    void testSuscribeEmployee_EmployeeAlreadySubscribed() {
        String type = "Promotion";
        String emailEmployee = "employee@example.com";

        // Simular que la notificación existe
        Notification notification = Notification.builder().type(type).build();
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(notification);

        // Simular que el empleado ya está suscrito a la notificación
        EmployeeNotification existingSubscription = EmployeeNotification.builder().emailEmployee(emailEmployee).notification(notification).build();
        when(employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(notification, emailEmployee)).thenReturn(existingSubscription);

        // Verificar que se lanza una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            suscribeEmployeeUseCase.suscribeEmployee(type, emailEmployee);
        });

        assertEquals("La notificacion ya esta asignada al empleado", exception.getMessage());

        // Verificar que no se intenta crear una nueva suscripción
        verify(employeeNotificationDbOutputAdapter, never()).suscribeEmployee(any(EmployeeNotification.class));
    }*/

    @Test
    void testSuscribeEmployee_InvalidNotificationType() {
        String type = ""; // Tipo de notificación inválido (vacío)
        String emailEmployee = "employee@example.com";

        // Verificar que se lanza una excepción si el tipo de notificación es inválido
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            suscribeEmployeeUseCase.suscribeEmployee(type, emailEmployee);
        });

        assertEquals("Tiene que haber un tipo de notificacion para agregarla", exception.getMessage());

        // Verificar que no se llama al método de suscripción
        verify(employeeNotificationDbOutputAdapter, never()).suscribeEmployee(any(EmployeeNotification.class));
    }

    @Test
    void testSuscribeEmployee_InvalidEmployeeEmail() {
        String type = "Promotion";
        String emailEmployee = "invalid-email"; // Email inválido

        // Simular que la notificación existe
        Notification notification = Notification.builder().type(type).build();
        when(notificationDbOutputAdapter.findNotificationByType(type)).thenReturn(notification);

        // Simular que el email del empleado no es válido
        when(notificationRestApiOutputAdapter.checkEmailEmployee(emailEmployee)).thenReturn(false);

        // Verificar que se lanza una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            suscribeEmployeeUseCase.suscribeEmployee(type, emailEmployee);
        });

        assertEquals("El email del empleado es incorrecto o no devuelve informacion", exception.getMessage());

        // Verificar que no se intenta crear una nueva suscripción
        verify(employeeNotificationDbOutputAdapter, never()).suscribeEmployee(any(EmployeeNotification.class));
    }
    
}
