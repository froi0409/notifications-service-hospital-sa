package com.sa.notifications.notification.application.sendmailsubjectnotificationusecase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMailSubjectNotificationRequest {
    private String email;
    private String subject;
    private String description;
}
