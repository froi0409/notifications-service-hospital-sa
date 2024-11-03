package com.sa.notifications.notification.application.sendallnotificationusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db.EmployeeNotificationLogDbOutputAdapter;
import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbOutputAdapter;
import com.sa.notifications.notification.application.sendemailusecase.SendEmailUseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendHiringNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendToAllNotificationInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class SendAllNotificationUseCase implements SendToAllNotificationInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;
    private NotificationLogDbOutputAdapter notificationLogDbOutputAdapter;
    private EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter;
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;
    private SendEmailUseCase sendEmailUseCase;

    @Autowired
    public SendAllNotificationUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter, NotificationLogDbOutputAdapter notificationLogDbOutputAdapter, EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter, EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter, SendEmailUseCase sendEmailUseCase) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
        this.notificationLogDbOutputAdapter = notificationLogDbOutputAdapter;
        this.employeeNotificationLogDbOutputAdapter = employeeNotificationLogDbOutputAdapter;
        this.employeeNotificationDbOutputAdapter = employeeNotificationDbOutputAdapter;
        this.sendEmailUseCase = sendEmailUseCase;
    }

    
    
    @Override
    public void sendToAllSuscribersNotification(String typeNotification, String description) {
        // Get Notification hiring
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(typeNotification);
        
        if(notification == null){
            throw new IllegalArgumentException("Tiene que haber un tipo de notificacion para mandar la notificacion");
        }
        
        List<EmployeeNotification> employees = this.employeeNotificationDbOutputAdapter.findEmployeeNotificationByNotificationId(notification);
        
        
        // Create a Notification log, by type
        NotificationLog notificationLog = NotificationLog.builder()
            .notification(notification)
            .description(description)
            .date(LocalDateTime.now())
            .build();
        
        notificationLog = this.notificationLogDbOutputAdapter.saveNotificationLog(notificationLog);
        
        for (EmployeeNotification employee : employees) {
            EmployeeNotificationLog employeeNotificationLog = EmployeeNotificationLog.builder()
                .notification(notificationLog)
                .emailEmployee(employee.getEmailEmployee())
                .build();
        
            employeeNotificationLog = this.employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(employeeNotificationLog);
        
            // Send email
            this.sendEmailUseCase.sendEmail(employee.getEmailEmployee(), "Subject: " + typeNotification, description);
        }        

    }

    

}
