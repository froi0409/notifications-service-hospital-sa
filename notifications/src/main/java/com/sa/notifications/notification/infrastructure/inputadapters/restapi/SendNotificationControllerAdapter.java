package com.sa.notifications.notification.infrastructure.inputadapters.restapi;


import com.sa.notifications.common.WebAdapter;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendHiringNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SendToAllNotificationInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
@WebAdapter
public class SendNotificationControllerAdapter {

    private SendHiringNotificationInputPort sendHiringNotificationInputPort;
    private SendToAllNotificationInputPort sendToAllNotificationInputPort;

    @Autowired
    public SendNotificationControllerAdapter(SendHiringNotificationInputPort sendHiringNotificationInputPort, SendToAllNotificationInputPort sendToAllNotificationInputPort) {
        this.sendHiringNotificationInputPort = sendHiringNotificationInputPort;
        this.sendToAllNotificationInputPort = sendToAllNotificationInputPort;
    }
    
    @PostMapping("/hiring")
    public void sendHiringNotification (
            @RequestParam String email){
        this.sendHiringNotificationInputPort.sendHiringNotification(email);
    }

    @PostMapping("/all/{type}")
    public void sendAllNotification (
            @PathVariable String type,
            @RequestParam String description){
        this.sendToAllNotificationInputPort.sendToAllSuscribersNotification(type, description);
    }

}
