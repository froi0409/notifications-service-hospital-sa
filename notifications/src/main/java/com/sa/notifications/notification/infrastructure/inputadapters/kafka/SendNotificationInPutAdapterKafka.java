package com.sa.notifications.notification.infrastructure.inputadapters.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.notifications.notification.application.sendallnotificationusecase.SendAllNotificationRequest;
import com.sa.notifications.notification.application.sendhiringnotificationusecase.SendHiringNotificationRequest;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendHiringNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendToAllNotificationInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationInPutAdapterKafka {
    
    private SendHiringNotificationInputPort sendHiringNotificationInputPort;
    private SendToAllNotificationInputPort sendToAllNotificationInputPort;

    @Autowired
    public SendNotificationInPutAdapterKafka(SendHiringNotificationInputPort sendHiringNotificationInputPort, SendToAllNotificationInputPort sendToAllNotificationInputPort,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.sendHiringNotificationInputPort = sendHiringNotificationInputPort;
        this.sendToAllNotificationInputPort = sendToAllNotificationInputPort;
    }

    @KafkaListener(topics = "send-hired", groupId = "notification-group")
    public void handleHiredNotificationEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SendHiringNotificationRequest request = objectMapper.readValue(message, SendHiringNotificationRequest.class);
        this.sendHiringNotificationInputPort.sendHiringNotification(request.getEmail(),request.getDescription());
    }
    
    @KafkaListener(topics = "send-all-by-type", groupId = "notification-group")
    public void handleSendNotificationByTypeEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SendAllNotificationRequest request = objectMapper.readValue(message, SendAllNotificationRequest.class);
        this.sendToAllNotificationInputPort.sendToAllSuscribersNotification(request.getType(),request.getDescription());
    }

}
