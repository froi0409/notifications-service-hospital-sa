package com.sa.notifications.lognotification.infrastructure.outputadapters.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogDbEntityRepository extends JpaRepository<NotificationLogDbEntity, String>{
    
}

