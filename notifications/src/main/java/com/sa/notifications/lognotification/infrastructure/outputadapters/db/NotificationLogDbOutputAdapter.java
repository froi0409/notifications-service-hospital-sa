package com.sa.notifications.lognotification.infrastructure.outputadapters.db;

import com.sa.notifications.common.OutputAdapter;
import com.sa.notifications.lognotification.domain.NotificationLog;
import org.springframework.beans.factory.annotation.Autowired;
import com.sa.notifications.lognotification.infrastructure.outputports.db.SaveNotificationLogOutputPort;


@OutputAdapter
public class NotificationLogDbOutputAdapter implements 
        SaveNotificationLogOutputPort{
    
    private NotificationLogDbEntityRepository notificationLogDbEntityRepository;
    
    @Autowired
    public NotificationLogDbOutputAdapter(NotificationLogDbEntityRepository notificationLogDbEntityRepository) {
        this.notificationLogDbEntityRepository = notificationLogDbEntityRepository;
    }

    @Override
    public NotificationLog saveNotificationLog(NotificationLog notificationLog) {
        NotificationLogDbEntity notificationDbEntity = NotificationLogDbEntity.from(notificationLog);
        return this.notificationLogDbEntityRepository.save(notificationDbEntity).toDomainModel();
    }

    

}
