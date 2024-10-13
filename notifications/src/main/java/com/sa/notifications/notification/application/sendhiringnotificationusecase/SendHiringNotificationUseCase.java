package com.sa.notifications.notification.application.sendhiringnotificationusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db.EmployeeNotificationLogDbOutputAdapter;
import com.sa.notifications.lognotification.domain.NotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbOutputAdapter;
import com.sa.notifications.notification.application.sendemailusecase.SendEmailUseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendHiringNotificationInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class SendHiringNotificationUseCase implements SendHiringNotificationInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;
    private NotificationLogDbOutputAdapter notificationLogDbOutputAdapter;
    private EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter;
    private SendEmailUseCase sendEmailUseCase;

    @Autowired
    public SendHiringNotificationUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter, NotificationLogDbOutputAdapter notificationLogDbOutputAdapter, EmployeeNotificationLogDbOutputAdapter employeeNotificationLogDbOutputAdapter, SendEmailUseCase sendEmailUseCase) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
        this.notificationLogDbOutputAdapter = notificationLogDbOutputAdapter;
        this.employeeNotificationLogDbOutputAdapter = employeeNotificationLogDbOutputAdapter;
        this.sendEmailUseCase = sendEmailUseCase;
    }

    @Override
    public void sendHiringNotification(String email) {
        
        // Get Notification hiring
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType("Hiring");
        
        // If there is no Notification hiring create one
        if(notification == null){
            notification = Notification.builder()
                .type("Hiring")
                .build();
            
            notification = this.notificationDbOutputAdapter.newNotification(notification);
        }
        
        // Create a Notification log, type hiring
        NotificationLog notificationLog = NotificationLog.builder()
            .notification(notification)
            .description(descriptionEmailHiring())
            .date(LocalDateTime.now())
            .build();
        
        notificationLog = this.notificationLogDbOutputAdapter.saveNotificationLog(notificationLog);
        
        // Create NotificationEmployee log
        EmployeeNotificationLog employeeNotificationLog = EmployeeNotificationLog.builder()
                .notification(notificationLog)
                .emailEmployee(email)
                .build();
        
        employeeNotificationLog = this.employeeNotificationLogDbOutputAdapter.saveEmployeeNotificationLog(employeeNotificationLog);
        
        // Send email
        this.sendEmailUseCase.sendEmail(email, "Welcome Employee", descriptionEmailHiring());
    }
    
    private String descriptionEmailHiring(){
        return "¡Hola!,\n Te informamos que a partir de hoy formas parte de nuestro equipo"
                + "\n ¡Te damos la bienvenida! \n Atentamente, HOSPITAL";
    }

}
