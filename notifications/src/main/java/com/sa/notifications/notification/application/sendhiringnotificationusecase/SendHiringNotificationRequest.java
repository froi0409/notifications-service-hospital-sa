package com.sa.notifications.notification.application.sendhiringnotificationusecase;

import lombok.Value;

@Value
public class SendHiringNotificationRequest {
    private String email;
    private String description;
}
