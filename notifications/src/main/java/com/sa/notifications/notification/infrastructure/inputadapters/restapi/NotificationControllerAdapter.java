package com.sa.notifications.notification.infrastructure.inputadapters.restapi;


import com.sa.notifications.common.WebAdapter;
import com.sa.notifications.notification.infrastructure.inputports.restapi.NewNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UpdateNotificationInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
@WebAdapter
public class NotificationControllerAdapter {

    private NewNotificationInputPort newNotificationInputPort;
    private UpdateNotificationInputPort updateNotificationInputPort;

    @Autowired
    public NotificationControllerAdapter(NewNotificationInputPort newNotificationInputPort, UpdateNotificationInputPort updateNotificationInputPort) {
        this.newNotificationInputPort = newNotificationInputPort;
        this.updateNotificationInputPort = updateNotificationInputPort;
    }
 
    @PostMapping("/type/{type}")
    public void newNotificationEmployee (@PathVariable String type){
        this.newNotificationInputPort.newNotification(type);
    }
    
    @PutMapping("/type/{oldtype}/{newtype}")
    public void newNotificationEmployee (@PathVariable String oldtype,@PathVariable String newtype){
        this.updateNotificationInputPort.updateNotification(newtype, oldtype);
    }

}
