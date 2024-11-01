package com.sa.notifications.notification.application.sendmailsubjectnotificationusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db.EmployeeNotificationLogDbOutputAdapter;
import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbOutputAdapter;
import com.sa.notifications.notification.application.sendemailusecase.SendEmailUseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendMailSubjectNotificationInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class SendMailSubjectNotificationUseCase implements SendMailSubjectNotificationInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;
    private NotificationLogDbOutputAdapter notificationLogDbOutputAdapter;
    private EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter;
    private SendEmailUseCase sendEmailUseCase;

    @Autowired
    public SendMailSubjectNotificationUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter, NotificationLogDbOutputAdapter notificationLogDbOutputAdapter, EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter, SendEmailUseCase sendEmailUseCase) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
        this.notificationLogDbOutputAdapter = notificationLogDbOutputAdapter;
        this.employeeNotificationLogDbOutputAdapter = employeeNotificationLogDbOutputAdapter;
        this.sendEmailUseCase = sendEmailUseCase;
    }

    @Override
    public void sendMailSubjectNotification(SendMailSubjectNotificationRequest request) {
        // Get Notification hiring
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(request.getSubject());
        
        // If there is no Notification hiring create one
        if(notification == null){
            notification = Notification.builder()
                .type(request.getSubject())
                .build();
            
            notification = this.notificationDbOutputAdapter.newNotification(notification);
        }
        
        // Create a Notification log, type hiring
        NotificationLog notificationLog = NotificationLog.builder()
            .notification(notification)
            .description(request.getDescription())
            .date(LocalDateTime.now())
            .build();
        
        notificationLog = this.notificationLogDbOutputAdapter.saveNotificationLog(notificationLog);
        
        // Create NotificationEmployee log
        EmployeeNotificationLog employeeNotificationLog = EmployeeNotificationLog.builder()
                .notification(notificationLog)
                .emailEmployee(request.getEmail())
                .build();
        
        employeeNotificationLog = this.employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(employeeNotificationLog);
        
        // Send email
        this.sendEmailUseCase.sendEmail(request.getEmail(), request.getSubject(), request.getDescription());
    }
    

}
