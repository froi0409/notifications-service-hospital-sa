package com.sa.notifications.notification.infrastructure.inputadapters.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.notifications.notification.application.sendallnotificationusecase.SendAllNotificationRequest;
import com.sa.notifications.notification.application.sendhiringnotificationusecase.SendHiringNotificationRequest;
import com.sa.notifications.notification.application.sendmailsubjectnotificationusecase.SendMailSubjectNotificationRequest;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendHiringNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendMailSubjectNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendToAllNotificationInputPort;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SendNotificationInPutAdapterKafkaTest {

    @Mock
    private SendHiringNotificationInputPort sendHiringNotificationInputPort;

    @Mock
    private SendToAllNotificationInputPort sendToAllNotificationInputPort;

    @Mock
    private SendMailSubjectNotificationInputPort sendMailSubjectNotificationInputPort;

    @InjectMocks
    private SendNotificationInPutAdapterKafka sendNotificationInPutAdapterKafka;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testHandleHiredNotificationEvent() throws JsonProcessingException {
        // Given
        SendHiringNotificationRequest request = new SendHiringNotificationRequest("test@example.com", "Welcome!");
        String message = objectMapper.writeValueAsString(request);

        // When
        sendNotificationInPutAdapterKafka.handleHiredNotificationEvent(message);

        // Then
        verify(sendHiringNotificationInputPort, times(1)).sendHiringNotification(request.getEmail(), request.getDescription());
    }


    @Test
    void testHandleMailSubjectNotificationEvent() throws JsonProcessingException {
        // Given
        SendMailSubjectNotificationRequest request = new SendMailSubjectNotificationRequest("Password Reset", "Please reset your password.", "test@example.com");
        String message = objectMapper.writeValueAsString(request);

        // When
        sendNotificationInPutAdapterKafka.handleMailSubjectNotificationEvent(message);

        // Then
        verify(sendMailSubjectNotificationInputPort, times(1)).sendMailSubjectNotification(argThat(argument ->
                argument.getSubject().equals(request.getSubject()) &&
                argument.getDescription().equals(request.getDescription()) &&
                argument.getEmail().equals(request.getEmail())
        ));
    }

    @Test
    void testHandleHiredNotificationEvent_WithException() {
        // Given
        String invalidMessage = "invalid message";

        // When/Then
        assertThrows(RuntimeException.class, () -> sendNotificationInPutAdapterKafka.handleHiredNotificationEvent(invalidMessage));
    }

    @Test
    void testHandleSendNotificationByTypeEvent_WithException() {
        // Given
        String invalidMessage = "invalid message";

        // When/Then
        assertThrows(RuntimeException.class, () -> sendNotificationInPutAdapterKafka.handleSendNotificationByTypeEvent(invalidMessage));
    }

    @Test
    void testHandleMailSubjectNotificationEvent_WithException() {
        // Given
        String invalidMessage = "invalid message";

        // When/Then
        assertThrows(RuntimeException.class, () -> sendNotificationInPutAdapterKafka.handleMailSubjectNotificationEvent(invalidMessage));
    }
}
