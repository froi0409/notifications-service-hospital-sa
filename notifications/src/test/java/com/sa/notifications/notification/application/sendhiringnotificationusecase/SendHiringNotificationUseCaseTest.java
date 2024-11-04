package com.sa.notifications.notification.application.sendhiringnotificationusecase;

import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db.EmployeeNotificationLogDbOutputAdapter;
import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbOutputAdapter;
import com.sa.notifications.notification.application.sendemailusecase.SendEmailUseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SendHiringNotificationUseCaseTest {

    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Mock
    private NotificationLogDbOutputAdapter notificationLogDbOutputAdapter;

    @Mock
    private EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter;

    @Mock
    private SendEmailUseCase sendEmailUseCase;

    @InjectMocks
    private SendHiringNotificationUseCase sendHiringNotificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendHiringNotification_CreateNewNotification() {
        // Given
        String email = "test@example.com";
        String description = "Welcome new employee";
        Notification newNotification = Notification.builder().id(UUID.randomUUID()).type("Hiring").build();
        NotificationLog newNotificationLog = NotificationLog.builder().id(UUID.randomUUID()).description(description).date(LocalDateTime.now()).build();
        EmployeeNotificationLog newEmployeeNotificationLog = EmployeeNotificationLog.builder().notification(newNotificationLog).emailEmployee(email).build();

        when(notificationDbOutputAdapter.findNotificationByType("Hiring")).thenReturn(null);
        when(notificationDbOutputAdapter.newNotification(any(Notification.class))).thenReturn(newNotification);
        when(notificationLogDbOutputAdapter.saveNotificationLog(any(NotificationLog.class))).thenReturn(newNotificationLog);
        when(employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(any(EmployeeNotificationLog.class))).thenReturn(newEmployeeNotificationLog);

        // When
        sendHiringNotificationUseCase.sendHiringNotification(email, description);

        // Then
        verify(notificationDbOutputAdapter, times(1)).newNotification(any(Notification.class));
        verify(notificationLogDbOutputAdapter, times(1)).saveNotificationLog(any(NotificationLog.class));
        verify(employeeNotificationLogDbOutputAdapter, times(1)).saveEmployeeNotificationLog(any(EmployeeNotificationLog.class));
        verify(sendEmailUseCase, times(1)).sendEmail(email, "Bienvenido Empleado", description);
    }

    @Test
    void testSendHiringNotification_ExistingNotification() {
        // Given
        String email = "test@example.com";
        String description = "Welcome new employee";
        Notification existingNotification = Notification.builder().id(UUID.randomUUID()).type("Hiring").build();
        NotificationLog existingNotificationLog = NotificationLog.builder().id(UUID.randomUUID()).description(description).date(LocalDateTime.now()).build();
        EmployeeNotificationLog existingEmployeeNotificationLog = EmployeeNotificationLog.builder().notification(existingNotificationLog).emailEmployee(email).build();

        when(notificationDbOutputAdapter.findNotificationByType("Hiring")).thenReturn(existingNotification);
        when(notificationLogDbOutputAdapter.saveNotificationLog(any(NotificationLog.class))).thenReturn(existingNotificationLog);
        when(employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(any(EmployeeNotificationLog.class))).thenReturn(existingEmployeeNotificationLog);

        // When
        sendHiringNotificationUseCase.sendHiringNotification(email, description);

        // Then
        verify(notificationDbOutputAdapter, never()).newNotification(any(Notification.class));
        verify(notificationLogDbOutputAdapter, times(1)).saveNotificationLog(any(NotificationLog.class));
        verify(employeeNotificationLogDbOutputAdapter, times(1)).saveEmployeeNotificationLog(any(EmployeeNotificationLog.class));
        verify(sendEmailUseCase, times(1)).sendEmail(email, "Bienvenido Empleado", description);
    }
}
