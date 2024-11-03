package com.sa.notifications.logemployeenotification.infrastructure.outputports.db;

import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;

public interface SaveEmployeeNotificationLogOutputPort {
    EmployeeNotificationLog saveEmployeeNotificationLog(EmployeeNotificationLog employeeNotification);
}
