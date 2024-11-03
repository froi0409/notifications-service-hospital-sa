package com.sa.notifications.notification.application.suscribeemployeeusecase;

import jakarta.transaction.Transactional;

import com.sa.notifications.common.UseCase;
import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputadapters.db.EmployeeNotificationDbOutputAdapter;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SuscribeEmployeeInputPort;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbOutputAdapter;
import com.sa.notifications.notification.infrastructure.outputadapters.restapi.NotificationRestApiOutputAdapter;
import org.springframework.beans.factory.annotation.Autowired;


@UseCase
@Transactional
public class SuscribeEmployeeUseCase implements SuscribeEmployeeInputPort{
    
    private NotificationDbOutputAdapter notificationDbOutputAdapter;
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;
    private NotificationRestApiOutputAdapter notificationRestApiOutputAdapter;

    @Autowired
    public SuscribeEmployeeUseCase(NotificationDbOutputAdapter notificationDbOutputAdapter, EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter, NotificationRestApiOutputAdapter notificationRestApiOutputAdapter) {
        this.notificationDbOutputAdapter = notificationDbOutputAdapter;
        this.employeeNotificationDbOutputAdapter = employeeNotificationDbOutputAdapter;
        this.notificationRestApiOutputAdapter = notificationRestApiOutputAdapter;
    }

    @Override
    public void suscribeEmployee(String type, String emailEmployee) {
        // Validate that not exists the same notification
        validateNotification(type);
        
        validateEmployee(emailEmployee);
        
        Notification notification = this.notificationDbOutputAdapter.findNotificationByType(type);
        if(notification == null){
            throw new IllegalArgumentException("La notificacion tiene que existir para ser agregada");
        }
        
        EmployeeNotification employeeNotification = this.employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(notification, emailEmployee);
        if(employeeNotification != null){
            throw new IllegalArgumentException("La notificacion ya esta asignada al empleado");
        }
        
        employeeNotification = EmployeeNotification.builder()
                .emailEmployee(emailEmployee)
                .notification(notification)
                .build();
        
        this.employeeNotificationDbOutputAdapter.suscribeEmployee(employeeNotification);
        
    }

    
    
    private void validateNotification(String type){
        if(type == null || type.isBlank() || type.isEmpty()){
            throw new IllegalArgumentException("Tiene que haber un tipo de notificacion para agregarla");
        }
    }
    
    private void validateEmployee(String idEmployee){
        if (!this.notificationRestApiOutputAdapter.checkEmailEmployee(idEmployee)) {
            throw new IllegalArgumentException("El email del empleado es incorrecto o no devuelve informacion");
        }
    }

    
    
    


}
