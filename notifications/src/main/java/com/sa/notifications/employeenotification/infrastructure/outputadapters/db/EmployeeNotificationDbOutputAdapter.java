package com.sa.notifications.employeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.common.OutputAdapter;
import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.employeenotification.infrastructure.outputports.db.FindEmployeeNotificationByNotificationAndEmailOutputPort;
import com.sa.notifications.employeenotification.infrastructure.outputports.db.FindEmployeeNotificationByNotificationIdOutputPort;
import com.sa.notifications.employeenotification.infrastructure.outputports.db.SuscribeEmployeeNotificationOutputPort;
import com.sa.notifications.employeenotification.infrastructure.outputports.db.UnsuscribeEmployeeNotificationOutputPort;
import com.sa.notifications.notification.domain.Notification;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.stream.Collectors;


@OutputAdapter
public class EmployeeNotificationDbOutputAdapter implements FindEmployeeNotificationByNotificationAndEmailOutputPort,
        SuscribeEmployeeNotificationOutputPort, UnsuscribeEmployeeNotificationOutputPort, FindEmployeeNotificationByNotificationIdOutputPort{
    
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

    @Override
    public void unsuscribeEmployee(EmployeeNotification employeeNotification) {
        // Buscar la suscripción correspondiente
        Optional<EmployeeNotificationDbEntity> employeeNotificationDbEntity =
                employeeNotificationDbEntityRepository.findByNotificationIdAndEmailEmployee(
                        employeeNotification.getNotification().getType(),
                        employeeNotification.getEmailEmployee());

        // Si la suscripción existe, eliminarla
        employeeNotificationDbEntity.ifPresent(employeeNotificationDbEntityRepository::delete);
    }

    @Override
    public List<EmployeeNotification> findEmployeeNotificationByNotificationId(Notification notification) {
        return employeeNotificationDbEntityRepository.findByNotificationId(notification.getId().toString())
                .stream()
                .map(EmployeeNotificationDbEntity::toDomainModel)
                .collect(Collectors.toList());
    }
    
}
