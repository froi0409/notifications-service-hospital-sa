package com.sa.notifications.employeenotification.infrastructure.outputports.db;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.notification.domain.Notification;
import java.util.List;


public interface FindEmployeeNotificationByNotificationIdOutputPort {
    List<EmployeeNotification> findEmployeeNotificationByNotificationId(Notification notification);
}
