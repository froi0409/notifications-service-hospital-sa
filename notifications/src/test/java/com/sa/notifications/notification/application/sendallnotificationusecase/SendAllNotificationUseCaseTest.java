package com.sa.notifications.notification.application.sendallnotificationusecase;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db.EmployeeNotificationLogDbOutputAdapter;
import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbOutputAdapter;
import com.sa.notifications.notification.application.sendemailusecase.SendEmailUseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SendAllNotificationUseCaseTest {
@Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Mock
    private NotificationLogDbOutputAdapter notificationLogDbOutputAdapter;

    @Mock
    private EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter;

    @Mock
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;

    @Mock
    private SendEmailUseCase sendEmailUseCase;

    @InjectMocks
    private SendAllNotificationUseCase sendAllNotificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendToAllSuscribersNotification() {
        // Datos simulados
        String typeNotification = "Hiring";
        String description = "Welcome to the new hire program.";
        
        // Simular una notificación
        Notification mockNotification = Notification.builder()
                .id(UUID.randomUUID())
                .type(typeNotification)
                .build();
        
        // Simular empleados suscritos
        EmployeeNotification mockEmployee1 = EmployeeNotification.builder()
                .id(UUID.randomUUID())
                .emailEmployee("employee1@example.com")
                .notification(mockNotification)
                .build();

        EmployeeNotification mockEmployee2 = EmployeeNotification.builder()
                .id(UUID.randomUUID())
                .emailEmployee("employee2@example.com")
                .notification(mockNotification)
                .build();
        
        List<EmployeeNotification> mockEmployees = List.of(mockEmployee1, mockEmployee2);

        // Simular la respuesta de obtener la notificación por tipo
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(mockNotification);

        // Simular la lista de empleados suscritos
        when(employeeNotificationDbOutputAdapter.findEmployeeNotificationByNotificationId(mockNotification)).thenReturn(mockEmployees);

        // Simular la creación del log de notificación
        NotificationLog mockNotificationLog = NotificationLog.builder()
                .notification(mockNotification)
                .description(description)
                .date(LocalDateTime.now())
                .build();
        
        when(notificationLogDbOutputAdapter.saveNotificationLog(any(NotificationLog.class)))
                .thenReturn(mockNotificationLog);

        // Ejecutar el caso de uso
        sendAllNotificationUseCase.sendToAllSuscribersNotification(typeNotification, description);

        // Verificar que se haya guardado el log de la notificación
        verify(notificationLogDbOutputAdapter, times(1)).saveNotificationLog(any(NotificationLog.class));

        // Verificar que se hayan enviado correos electrónicos a todos los empleados suscritos
        verify(sendEmailUseCase, times(1)).sendEmail("employee1@example.com", "Subject: Hiring", description);
        verify(sendEmailUseCase, times(1)).sendEmail("employee2@example.com", "Subject: Hiring", description);

        // Verificar que se hayan guardado los logs de notificación de empleados
        ArgumentCaptor<EmployeeNotificationLog> employeeLogCaptor = ArgumentCaptor.forClass(EmployeeNotificationLog.class);
        verify(employeeNotificationLogDbOutputAdapter, times(2)).saveEmployeeNotificationLog(employeeLogCaptor.capture());

        List<EmployeeNotificationLog> capturedLogs = employeeLogCaptor.getAllValues();
        assertEquals(2, capturedLogs.size());
        assertEquals("employee1@example.com", capturedLogs.get(0).getEmailEmployee());
        assertEquals("employee2@example.com", capturedLogs.get(1).getEmailEmployee());
    }

    @Test
    void testSendToAllSuscribersNotificationThrowsException() {
        String typeNotification = "InvalidType";
        String description = "This notification should fail.";

        // Simular que no existe notificación con el tipo proporcionado
        when(notificationDbOutputAdapter.findNotificationByType(typeNotification)).thenReturn(null);

        // Verificar que se lanza una excepción cuando no se encuentra la notificación
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sendAllNotificationUseCase.sendToAllSuscribersNotification(typeNotification, description);
        });

        assertEquals("Tiene que haber un tipo de notificacion para mandar la notificacion", exception.getMessage());

        // Verificar que no se realizaron otras interacciones
        verify(notificationLogDbOutputAdapter, never()).saveNotificationLog(any(NotificationLog.class));
        verify(sendEmailUseCase, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
