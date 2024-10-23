package com.sa.notifications.notification.infrastructure.inputports.restapi;

import com.sa.notifications.notification.domain.Notification;
import java.util.List;

public interface GetAllNotificationsInputPort {
    List<Notification> getAllNotification();
}
