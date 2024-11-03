package com.sa.notifications.lognotification.domain;

import com.sa.notifications.common.DomainEntity;
import com.sa.notifications.notification.domain.Notification;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@DomainEntity
public class NotificationLog {
    private UUID id;
    private Notification notification;
    private String description;
    private LocalDateTime date;
}

