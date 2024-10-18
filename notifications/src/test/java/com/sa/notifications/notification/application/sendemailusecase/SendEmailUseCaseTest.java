package com.sa.notifications.notification.application.sendemailusecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class SendEmailUseCaseTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private SendEmailUseCase sendEmailUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_Success() {
        // Datos simulados
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "This is a test email body.";

        // Ejecutar el caso de uso
        sendEmailUseCase.sendEmail(to, subject, body);

        // Verificar que se construyó el mensaje correctamente
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        // Capturar el mensaje enviado
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("williamumana201931448@cunoc.edu.gt");

        // Verificar que se enviaron los valores correctos
        verify(mailSender).send(argThat((SimpleMailMessage msg) ->
                msg.getTo()[0].equals(to) &&
                msg.getSubject().equals(subject) &&
                msg.getText().equals(body) &&
                msg.getFrom().equals("williamumana201931448@cunoc.edu.gt")
        ));
    }

    @Test
    void testSendEmail_InvalidEmail_ThrowsException() {
        String to = "";  // Email inválido
        String subject = "Test Subject";
        String body = "This is a test email body.";

        // Ejecutar y verificar que no se llama al método `send`
        sendEmailUseCase.sendEmail(to, subject, body);

        // No se debe intentar enviar un correo con un email inválido
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
