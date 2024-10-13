package com.sa.notifications.employeenotification.infrastructure.outputports.db;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.notification.domain.Notification;


public interface FindEmployeeNotificationByNotificationAndEmailOutputPort {
    EmployeeNotification FindEmployeeNotificationByEmailAndEmployee(Notification notification, String email);
}
