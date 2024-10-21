package com.sa.notifications.notification.application.sendallnotificationusecase;

import lombok.Value;

@Value
public class SendAllNotificationRequest {
    private String type;
    private String description;
}
