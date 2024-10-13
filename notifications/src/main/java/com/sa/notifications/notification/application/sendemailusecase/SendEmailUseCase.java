package com.sa.notifications.notification.application.sendemailusecase;

import com.sa.notifications.common.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@UseCase
public class SendEmailUseCase {
    
    private final JavaMailSender mailSender;
    
    @Autowired
    public SendEmailUseCase(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("williamumana201931448@cunoc.edu.gt");  // Asegúrate de que el correo sea el mismo que el configurado en properties

        mailSender.send(message);
    }
    
}
