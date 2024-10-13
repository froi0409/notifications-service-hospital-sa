package com.sa.notifications.notification.infrastructure.inputports.restapi;

public interface SendToAllNotificationInputPort {
    void sendToAllSuscribersNotification(String typeNotification, String description);
}
