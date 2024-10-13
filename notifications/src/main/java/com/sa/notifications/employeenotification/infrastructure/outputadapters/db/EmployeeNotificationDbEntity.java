package com.sa.notifications.employeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.notification.infrastructure.outputadapters.db.NotificationDbEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "employee_notification", schema = "notifications")
public class EmployeeNotificationDbEntity {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_notification", nullable = false)
    private NotificationDbEntity notification;

    @Column(name = "email_employee", nullable = false)
    private String emailEmployee;

    // Conversión a modelo de dominio
    public EmployeeNotification toDomainModel() {
        return EmployeeNotification.builder()
                .id(UUID.fromString(this.id))
                .notification(this.notification.toDomainModel())
                .emailEmployee(this.emailEmployee)
                .build();
    }

    // Conversión desde el modelo de dominio
    public static EmployeeNotificationDbEntity from(EmployeeNotification employeeNotification) {
        EmployeeNotificationDbEntity entity = new EmployeeNotificationDbEntity();
        entity.setId(employeeNotification.getId() != null ? employeeNotification.getId().toString() : UUID.randomUUID().toString());
        entity.setNotification(NotificationDbEntity.from(employeeNotification.getNotification()));
        entity.setEmailEmployee(employeeNotification.getEmailEmployee());
        return entity;
    }
}