package com.sa.notifications.notification.infrastructure.inputadapters.restapi.response;

import com.sa.notifications.notification.domain.Notification;
import lombok.Value;

@Value
public class NotificationResponse {
    private String id;
    private String type;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId().toString();
        this.type = notification.getType();
    }
    
    
}
