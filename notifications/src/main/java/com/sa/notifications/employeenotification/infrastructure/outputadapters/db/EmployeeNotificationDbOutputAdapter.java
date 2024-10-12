package com.sa.notifications.employeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.common.OutputAdapter;
import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputports.db.FindEmployeeNotificationByNotificationAndEmailOutputPort;
import com.sa.notifications.employeenotification.infrastructure.outputports.db.SuscribeEmployeeNotificationOutputPort;
import com.sa.notifications.notification.domain.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;


@OutputAdapter
public class EmployeeNotificationDbOutputAdapter implements FindEmployeeNotificationByNotificationAndEmailOutputPort,
        SuscribeEmployeeNotificationOutputPort{
    
    private EmployeeNotificationDbEntityRepository employeeNotificationDbEntityRepository;
    
    @Autowired
    public EmployeeNotificationDbOutputAdapter(EmployeeNotificationDbEntityRepository employeeNotificationDbEntityRepository) {
        this.employeeNotificationDbEntityRepository = employeeNotificationDbEntityRepository;
    }

    @Override
    public EmployeeNotification FindEmployeeNotificationByEmailAndEmployee(Notification notification, String email) {
        Optional<EmployeeNotificationDbEntity> employeeNotificationDbEntityOpt = this.employeeNotificationDbEntityRepository.findByNotificationIdAndEmailEmployee(notification.getId().toString(), email);
        if (employeeNotificationDbEntityOpt.isPresent()) {
            return employeeNotificationDbEntityOpt.get().toDomainModel();
        }
        return null;
    }

    @Override
    public EmployeeNotification suscribeEmployee(EmployeeNotification employeeNotification) {
        EmployeeNotificationDbEntity notificationDbEntity = EmployeeNotificationDbEntity.from(employeeNotification);
        return this.employeeNotificationDbEntityRepository.save(notificationDbEntity).toDomainModel();
    }

}
