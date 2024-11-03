package com.sa.notifications.notification.infrastructure.outputports.db;

import com.sa.notifications.notification.domain.Notification;

public interface FindNotificationByTypeOutputPort {
    Notification findNotificationByType(String type);
}
