package com.sa.notifications.notification.infrastructure.inputports.restapi;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import java.util.List;

public interface GetAllSuscribersInputPort {
    List<EmployeeNotification> getAllSuscribers(String type);
}
