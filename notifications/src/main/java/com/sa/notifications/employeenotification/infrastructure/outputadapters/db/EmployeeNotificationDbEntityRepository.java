package com.sa.notifications.employeenotification.infrastructure.outputadapters.db;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeNotificationDbEntityRepository extends JpaRepository<EmployeeNotificationDbEntity, String>{
    Optional<EmployeeNotificationDbEntity> findByNotificationIdAndEmailEmployee(String notificationId, String emailEmployee);
}

