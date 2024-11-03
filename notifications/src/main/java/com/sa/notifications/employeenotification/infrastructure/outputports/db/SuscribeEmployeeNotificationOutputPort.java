package com.sa.notifications.employeenotification.infrastructure.outputports.db;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;

public interface SuscribeEmployeeNotificationOutputPort {
    EmployeeNotification suscribeEmployee(EmployeeNotification employeeNotification);
}
