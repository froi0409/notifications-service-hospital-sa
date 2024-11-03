package com.sa.notifications.notification.infrastructure.outputports.db;

import com.sa.notifications.notification.domain.Notification;
import java.util.List;

public interface GetAllNotificationsOutputPort {
    List<Notification> getAllNotifications();
}
