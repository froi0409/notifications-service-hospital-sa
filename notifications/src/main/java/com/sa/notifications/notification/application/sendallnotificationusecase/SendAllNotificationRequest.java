package com.sa.notifications.notification.application.sendallnotificationusecase;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class SendAllNotificationRequest {
    private String type;
    private String description;
}
