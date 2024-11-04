package com.sa.notifications.notification.application.sendmailsubjectnotificationusecase;


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

class SendMailSubjectNotificationUseCaseTest {

    @Mock
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Mock
    private NotificationLogDbOutputAdapter notificationLogDbOutputAdapter;

    @Mock
    private EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter;

    @Mock
    private SendEmailUseCase sendEmailUseCase;

    @InjectMocks
    private SendMailSubjectNotificationUseCase sendMailSubjectNotificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMailSubjectNotification_CreateNewNotification() {
        // Given
        SendMailSubjectNotificationRequest request = new SendMailSubjectNotificationRequest("Subject Test", "Test Description", "test@example.com");
        Notification newNotification = Notification.builder().id(UUID.randomUUID()).type(request.getSubject()).build();
        NotificationLog newNotificationLog = NotificationLog.builder().id(UUID.randomUUID()).description(request.getDescription()).date(LocalDateTime.now()).build();
        EmployeeNotificationLog newEmployeeNotificationLog = EmployeeNotificationLog.builder().notification(newNotificationLog).emailEmployee(request.getEmail()).build();

        when(notificationDbOutputAdapter.findNotificationByType(request.getSubject())).thenReturn(null);
        when(notificationDbOutputAdapter.newNotification(any(Notification.class))).thenReturn(newNotification);
        when(notificationLogDbOutputAdapter.saveNotificationLog(any(NotificationLog.class))).thenReturn(newNotificationLog);
        when(employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(any(EmployeeNotificationLog.class))).thenReturn(newEmployeeNotificationLog);

        // When
        sendMailSubjectNotificationUseCase.sendMailSubjectNotification(request);

        // Then
        verify(notificationDbOutputAdapter, times(1)).newNotification(any(Notification.class));
        verify(notificationLogDbOutputAdapter, times(1)).saveNotificationLog(any(NotificationLog.class));
        verify(employeeNotificationLogDbOutputAdapter, times(1)).saveEmployeeNotificationLog(any(EmployeeNotificationLog.class));
        verify(sendEmailUseCase, times(1)).sendEmail(request.getEmail(), request.getSubject(), request.getDescription());
    }

    @Test
    void testSendMailSubjectNotification_ExistingNotification() {
        // Given
        SendMailSubjectNotificationRequest request = new SendMailSubjectNotificationRequest("Subject Test", "Test Description", "test@example.com");
        Notification existingNotification = Notification.builder().id(UUID.randomUUID()).type(request.getSubject()).build();
        NotificationLog existingNotificationLog = NotificationLog.builder().id(UUID.randomUUID()).description(request.getDescription()).date(LocalDateTime.now()).build();
        EmployeeNotificationLog existingEmployeeNotificationLog = EmployeeNotificationLog.builder().notification(existingNotificationLog).emailEmployee(request.getEmail()).build();

        when(notificationDbOutputAdapter.findNotificationByType(request.getSubject())).thenReturn(existingNotification);
        when(notificationLogDbOutputAdapter.saveNotificationLog(any(NotificationLog.class))).thenReturn(existingNotificationLog);
        when(employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(any(EmployeeNotificationLog.class))).thenReturn(existingEmployeeNotificationLog);

        // When
        sendMailSubjectNotificationUseCase.sendMailSubjectNotification(request);

        // Then
        verify(notificationDbOutputAdapter, never()).newNotification(any(Notification.class));
        verify(notificationLogDbOutputAdapter, times(1)).saveNotificationLog(any(NotificationLog.class));
        verify(employeeNotificationLogDbOutputAdapter, times(1)).saveEmployeeNotificationLog(any(EmployeeNotificationLog.class));
        verify(sendEmailUseCase, times(1)).sendEmail(request.getEmail(), request.getSubject(), request.getDescription());
    }
}

