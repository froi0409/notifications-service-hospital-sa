package com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.logemployeenotification.domain.EmployeeNotificationLog;
import com.sa.notifications.lognotification.infrastructure.outputadapters.db.NotificationLogDbEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "employee_notification_log", schema = "notifications")
public class EmployeeNotificationLogDbEntity {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_notification", nullable = false)
    private NotificationLogDbEntity notification;

    @Column(name = "email_employee", nullable = false)
    private String emailEmployee;

    // Conversión a modelo de dominio
    public EmployeeNotificationLog toDomainModel() {
        return EmployeeNotificationLog.builder()
                .id(UUID.fromString(this.id))
                .notification(this.notification.toDomainModel())
                .emailEmployee(this.emailEmployee)
                .build();
    }

    // Conversión desde el modelo de dominio
    public static EmployeeNotificationLogDbEntity from(EmployeeNotificationLog employeeNotification) {
        return EmployeeNotificationLogDbEntity.builder()
                .id(employeeNotification.getId() != null ? employeeNotification.getId().toString() : UUID.randomUUID().toString())
                .notification(NotificationLogDbEntity.from(employeeNotification.getNotification()))
                .emailEmployee(employeeNotification.getEmailEmployee())
                .build();
    }
}