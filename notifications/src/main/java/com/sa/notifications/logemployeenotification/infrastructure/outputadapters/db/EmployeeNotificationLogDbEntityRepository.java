package com.sa.notifications.logemployeenotification.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeNotificationLogDbEntityRepository extends JpaRepository<EmployeeNotificationLogDbEntity, String>{
    
}

