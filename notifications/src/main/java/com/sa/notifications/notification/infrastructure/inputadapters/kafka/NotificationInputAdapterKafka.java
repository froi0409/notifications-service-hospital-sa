package com.sa.notifications.notification.infrastructure.inputadapters.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.notifications.notification.infrastructure.inputports.restapi.NewNotificationInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class NotificationInputAdapterKafka {
    
    private NewNotificationInputPort newNotificationInputPort;

    @Autowired
    public NotificationInputAdapterKafka(NewNotificationInputPort newNotificationInputPort,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.newNotificationInputPort = newNotificationInputPort;
    }

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, maxDelay = 3000)
    )
    @KafkaListener(topics = "new-notification", groupId = "notification-group")
    public void handleNewNotificationEvent(String message) throws JsonProcessingException {
        try {
            this.newNotificationInputPort.newNotification(message);
        } catch (Exception e) {
            throw new RuntimeException("Error processing message: " + message, e);
        }
        
    }
    

}
