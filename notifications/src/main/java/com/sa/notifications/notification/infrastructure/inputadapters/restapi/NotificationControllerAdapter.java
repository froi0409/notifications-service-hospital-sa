package com.sa.notifications.notification.infrastructure.inputadapters.restapi;


import com.sa.notifications.common.WebAdapter;
import com.sa.notifications.notification.infrastructure.inputadapters.restapi.response.NotificationResponse;
import com.sa.notifications.notification.infrastructure.inputports.restapi.GetAllNotificationsInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.NewNotificationInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UpdateNotificationInputPort;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    private GetAllNotificationsInputPort getAllNotificationsInputPort;

    @Autowired
    public NotificationControllerAdapter(NewNotificationInputPort newNotificationInputPort, UpdateNotificationInputPort updateNotificationInputPort,
        GetAllNotificationsInputPort getAllNotificationsInputPort) {
        this.newNotificationInputPort = newNotificationInputPort;
        this.updateNotificationInputPort = updateNotificationInputPort;
        this.getAllNotificationsInputPort = getAllNotificationsInputPort;
    }
 
    @PostMapping("/type/{type}")
    public void newNotificationEmployee (@PathVariable String type){
        this.newNotificationInputPort.newNotification(type);
    }
    
    @PutMapping("/type/{oldtype}/{newtype}")
    public void newNotificationEmployee (@PathVariable String oldtype,@PathVariable String newtype){
        this.updateNotificationInputPort.updateNotification(newtype, oldtype);
    }
    
    @GetMapping("/all")
    public List<NotificationResponse> getAllNotification(){
        return this.getAllNotificationsInputPort.getAllNotification()
                .stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

}
