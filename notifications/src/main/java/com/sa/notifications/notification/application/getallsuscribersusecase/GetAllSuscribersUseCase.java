package com.sa.notifications.notification.application.getallsuscribersusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.GetAllSuscribersInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class GetAllSuscribersUseCase implements GetAllSuscribersInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;

    @Autowired
    public GetAllSuscribersUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter, EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
        this.employeeNotificationDbOutputAdapter = employeeNotificationDbOutputAdapter;
    }

    @Override
    public List<EmployeeNotification> getAllSuscribers(String type) {
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(type);
        
        if(notification == null){
            throw new IllegalArgumentException("Tiene que haber un tipo de notificacion para buscar suscriptores");
        }
        
        List<EmployeeNotification> employees = this.employeeNotificationDbOutputAdapter.findEmployeeNotificationByNotificationId(notification);
        return employees;
    }

    

}
