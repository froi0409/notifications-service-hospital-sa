package com.sa.notifications.notification.infrastructure.inputadapters.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.notifications.notification.application.sendallnotificationusecase.SendAllNotificationRequest;
import com.sa.notifications.notification.application.sendhiringnotificationusecase.SendHiringNotificationRequest;
import com.sa.notifications.notification.application.sendmailsubjectnotificationusecase.SendMailSubjectNotificationRequest;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendHiringNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendMailSubjectNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendToAllNotificationInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationInPutAdapterKafka {
    
    private SendHiringNotificationInputPort sendHiringNotificationInputPort;
    private SendToAllNotificationInputPort sendToAllNotificationInputPort;
    private SendMailSubjectNotificationInputPort sendMailSubjectNotificationInputPort;

    @Autowired
    public SendNotificationInPutAdapterKafka(SendHiringNotificationInputPort sendHiringNotificationInputPort, 
            SendMailSubjectNotificationInputPort sendMailSubjectNotificationInputPort,
            SendToAllNotificationInputPort sendToAllNotificationInputPort,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.sendHiringNotificationInputPort = sendHiringNotificationInputPort;
        this.sendToAllNotificationInputPort = sendToAllNotificationInputPort;
        this.sendMailSubjectNotificationInputPort = sendMailSubjectNotificationInputPort;
    }

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, maxDelay = 3000)
    )
    @KafkaListener(topics = "send-hired", groupId = "notification-group")
    public void handleHiredNotificationEvent(String message) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SendHiringNotificationRequest request = objectMapper.readValue(message, SendHiringNotificationRequest.class);
            this.sendHiringNotificationInputPort.sendHiringNotification(request.getEmail(),request.getDescription());
        } catch (Exception e) {
            throw new RuntimeException("Error processing message: " + message, e);
        }
        
    }
    
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, maxDelay = 3000)
    )
    @KafkaListener(topics = "send-all-by-type", groupId = "notification-group")
    public void handleSendNotificationByTypeEvent(String message) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SendAllNotificationRequest request = objectMapper.readValue(message, SendAllNotificationRequest.class);
            this.sendToAllNotificationInputPort.sendToAllSuscribersNotification(request.getType(),request.getDescription());
        } catch (Exception e) {
            throw new RuntimeException("Error processing message: " + message, e);
        }
    }
    
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, maxDelay = 3000)
    )
    @KafkaListener(topics = "send-forgot-password", groupId = "notification-group")
    public void handleMailSubjectNotificationEvent(String message) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SendMailSubjectNotificationRequest request = objectMapper.readValue(message, SendMailSubjectNotificationRequest.class);
            this.sendMailSubjectNotificationInputPort.sendMailSubjectNotification(request);
        } catch (Exception e) {
            throw new RuntimeException("Error processing message: " + message, e);
        }
        
    }
    

}
