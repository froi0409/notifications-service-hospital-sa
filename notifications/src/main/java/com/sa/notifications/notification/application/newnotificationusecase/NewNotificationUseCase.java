package com.sa.notifications.notification.application.newnotificationusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.NewNotificationInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class NewNotificationUseCase implements NewNotificationInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Autowired
    public NewNotificationUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
    }

    @Override
    public void newNotification(String typeNotification) {
        // Validate that not exists the same notification
        validateIsNewNotification(typeNotification);
        // Create notification
        Notification notification = Notification.builder()
                .type(typeNotification)
                .build();
        
        this.notificationDbOutputAdapter.newNotification(notification);
    }
    
    private void validateIsNewNotification(String type){
        if(type == null || type.isBlank() || type.isEmpty()){
            throw new IllegalArgumentException("Tiene que haber un tipo de notificacion para agregarla");
        }
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(type);
        if(notification != null){
            throw new IllegalArgumentException("La notificacion tiene que ser nueva para poder agregarla");
        }
    }

}
