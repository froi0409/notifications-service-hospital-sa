package com.sa.notifications.notification.infrastructure.outputadapters.db;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDbEntityRepository extends JpaRepository<NotificationDbEntity, String>{
    List<NotificationDbEntity> findByType(String type);
}

