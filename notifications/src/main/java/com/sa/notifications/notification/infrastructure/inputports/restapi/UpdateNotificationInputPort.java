package com.sa.notifications.notification.infrastructure.inputports.restapi;


public interface UpdateNotificationInputPort {

    void updateNotification(String newType, String oldType);
}
