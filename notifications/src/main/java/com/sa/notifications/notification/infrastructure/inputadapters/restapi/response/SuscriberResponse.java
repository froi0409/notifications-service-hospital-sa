package com.sa.notifications.notification.infrastructure.inputadapters.restapi.response;


import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import lombok.Value;

@Value
public class SuscriberResponse {
    private String id;
    private String idNotification;
    private String type;
    private String emailEmployee;

    public SuscriberResponse(EmployeeNotification employeeNotification) {
        this.id = employeeNotification.getId().toString();
        this.idNotification = employeeNotification.getNotification().getId().toString();
        this.type = employeeNotification.getNotification().getType();
        this.emailEmployee = employeeNotification.getEmailEmployee();
    }
    
    
}
