package com.sa.notifications.notification.infrastructure.inputports.restapi;

import com.sa.notifications.notification.application.sendmailsubjectnotificationusecase.SendMailSubjectNotificationRequest;

public interface SendMailSubjectNotificationInputPort {
    void sendMailSubjectNotification(SendMailSubjectNotificationRequest request);
}
