package com.sa.notifications.notification.infrastructure.inputadapters.restapi;


import com.sa.notifications.common.WebAdapter;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SuscribeEmployeeInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UnsuscribeEmployeeInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
@WebAdapter
public class SuscribeNotificationControllerAdapter {

    private SuscribeEmployeeInputPort suscribeEmployeeInputPort;
    private UnsuscribeEmployeeInputPort unsuscribeEmployeeInputPort;

    @Autowired
    public SuscribeNotificationControllerAdapter(SuscribeEmployeeInputPort suscribeEmployeeInputPort, UnsuscribeEmployeeInputPort unsuscribeEmployeeInputPort) {
        this.suscribeEmployeeInputPort = suscribeEmployeeInputPort;
        this.unsuscribeEmployeeInputPort = unsuscribeEmployeeInputPort;
    }
    
    @PostMapping("/suscribe/{type}/{email}")
    public void suscribeEmployee (
            @PathVariable String type,
            @PathVariable String email){
        this.suscribeEmployeeInputPort.suscribeEmployee(type, email);
    }
    
    @PostMapping("/unsuscribe/{type}/{email}")
    public void unsuscribeEmployee (
            @PathVariable String type,
            @PathVariable String email){
        this.unsuscribeEmployeeInputPort.unsuscribeEmployee(type, email);
    }

}
