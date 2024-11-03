package com.sa.notifications.notification.infrastructure.outputports.db;

import com.sa.notifications.notification.domain.Notification;

public interface NewNotificationOutputPort {
    Notification newNotification(Notification notification);
}
