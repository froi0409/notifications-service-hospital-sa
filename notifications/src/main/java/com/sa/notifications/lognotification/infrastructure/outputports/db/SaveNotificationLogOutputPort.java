package com.sa.notifications.lognotification.infrastructure.outputports.db;

import com.sa.notifications.lognotification.domain.NotificationLog;

public interface SaveNotificationLogOutputPort {
    NotificationLog saveNotificationLog(NotificationLog employeeNotification);
}
