package com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.common.OutputAdapter;
import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.sa.notifications.logemployeenotification.infrastructure.outputports.db.SaveEmployeeNotificationLogOutputPort;


@OutputAdapter
public class EmployeeNotificationLogDbOutputAdapter implements 
        SaveEmployeeNotificationLogOutputPort{
    
    private EmployeeNotificationLogDbEntityRepository employeeNotificationDbEntityRepository;
    
    @Autowired
    public EmployeeNotificationLogDbOutputAdapter(EmployeeNotificationLogDbEntityRepository employeeNotificationDbEntityRepository) {
        this.employeeNotificationDbEntityRepository = employeeNotificationDbEntityRepository;
    }

    @Override
    public EmployeeNotificationLog saveEmployeeNotificationLog(EmployeeNotificationLog employeeNotification) {
        EmployeeNotificationLogDbEntity notificationDbEntity = EmployeeNotificationLogDbEntity.from(employeeNotification);
        return this.employeeNotificationDbEntityRepository.save(notificationDbEntity).toDomainModel();
    }

}
