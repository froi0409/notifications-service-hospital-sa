package com.sa.notifications.notification.application.getallnotificationsusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.GetAllNotificationsInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class GetAllNotificationsUseCase implements GetAllNotificationsInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;

    @Autowired
    public GetAllNotificationsUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationDbOutputAdapter.getAllNotifications();
    }

}
