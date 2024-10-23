package com.sa.notifications.notification.infrastructure.outputadapters.db;

import com.sa.notifications.common.OutputAdapter;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.outputports.db.FindNotificationByTypeOutputPort;
import com.sa.notifications.notification.infrastructure.outputports.db.GetAllNotificationsOutputPort;
import com.sa.notifications.notification.infrastructure.outputports.db.NewNotificationOutputPort;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.stream.Collectors;


@OutputAdapter
public class NotificationDbOutputAdapter implements NewNotificationOutputPort, FindNotificationByTypeOutputPort,
        GetAllNotificationsOutputPort{
    
    private NotificationDbEntityRepository notificationDbEntityRepository;
    
    @Autowired
    public NotificationDbOutputAdapter(NotificationDbEntityRepository notificationDbEntityRepository) {
        this.notificationDbEntityRepository = notificationDbEntityRepository;
    }

    @Override
    public Notification newNotification(Notification notification) {
        NotificationDbEntity notificationDbEntity = NotificationDbEntity.from(notification);
        return this.notificationDbEntityRepository.save(notificationDbEntity).toDomainModel();
    }

    @Override
    public Notification findNotificationByType(String type) {
        List<NotificationDbEntity> notificationsEntity = notificationDbEntityRepository.findByType(type);
        if (notificationsEntity.isEmpty()) {
            return null;
        }
        return notificationsEntity.get(0).toDomainModel();
    }
    
    public Notification updateNotification(Notification notification) {
        Optional<NotificationDbEntity> existingNotification = notificationDbEntityRepository.findById(notification.getId().toString());

        if (existingNotification.isPresent()) {
            NotificationDbEntity notificationDbEntity = existingNotification.get();
            notificationDbEntity.setType(notification.getType());
            NotificationDbEntity updatedNotification = notificationDbEntityRepository.save(notificationDbEntity);

            return updatedNotification.toDomainModel();
        }

        return null;
    }
    
    @Override
    public List<Notification> getAllNotifications() {
        return notificationDbEntityRepository.findAll().stream()
            .map(NotificationDbEntity::toDomainModel)
            .collect(Collectors.toList());
    }

}
