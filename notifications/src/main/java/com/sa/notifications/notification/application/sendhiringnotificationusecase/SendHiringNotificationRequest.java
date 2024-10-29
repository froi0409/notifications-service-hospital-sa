package com.sa.notifications.notification.application.sendhiringnotificationusecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendHiringNotificationRequest {
    private String email;
    private String description;
}
