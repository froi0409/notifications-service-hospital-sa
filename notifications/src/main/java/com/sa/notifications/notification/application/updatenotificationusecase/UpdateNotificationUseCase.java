package com.sa.notifications.notification.application.updatenotificationusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UpdateNotificationInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class UpdateNotificationUseCase implements UpdateNotificationInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Autowired
    public UpdateNotificationUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
    }
    
    @Override
    public void updateNotification(String newType, String oldType) {
        // Validate old type notification
        validateOldTypeNotification(oldType);
        
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(oldType);
        if(notification == null){
            throw new IllegalArgumentException("La notificacion de tipo"+ oldType + " tiene que existir para poder actualizarla");
        }
        
        // Validate new type notification
        validateIsNewNotification(newType);
        
        // Update notification
        notification.setType(newType);

        this.notificationDbOutputAdapter.updateNotification(notification);
    }
    
    private void validateOldTypeNotification(String type){
        if(type == null || type.isBlank() || type.isEmpty()){
            throw new IllegalArgumentException("Tiene que haber un tipo antiguo de notificacion para modificarla");
        }
    }
    
    private void validateIsNewNotification(String type){
        if(type == null || type.isBlank() || type.isEmpty()){
            throw new IllegalArgumentException("Tiene que haber un tipo nuevo de notificacion para modificarla");
        }
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(type);
        if(notification != null){
            throw new IllegalArgumentException("La notificacion de tipo " +type+ " tiene que ser nueva para poder agregarla");
        }
    }

    

}
