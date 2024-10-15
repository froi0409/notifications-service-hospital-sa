package com.sa.notifications.notification.infrastructure.inputadapters.restapi;


import com.sa.notifications.common.WebAdapter;
import com.sa.notifications.notification.infrastructure.inputadapters.restapi.response.SuscriberResponse;
import com.sa.notifications.notification.infrastructure.inputports.restapi.GetAllSuscribersInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SuscribeEmployeeInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UnsuscribeEmployeeInputPort;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications")
@WebAdapter
public class SuscribeNotificationControllerAdapter {

    private SuscribeEmployeeInputPort suscribeEmployeeInputPort;
    private UnsuscribeEmployeeInputPort unsuscribeEmployeeInputPort;
    private GetAllSuscribersInputPort getAllSuscribersInputPort;

    @Autowired
    public SuscribeNotificationControllerAdapter(SuscribeEmployeeInputPort suscribeEmployeeInputPort, UnsuscribeEmployeeInputPort unsuscribeEmployeeInputPort,
            GetAllSuscribersInputPort getAllSuscribersInputPort) {
        this.suscribeEmployeeInputPort = suscribeEmployeeInputPort;
        this.unsuscribeEmployeeInputPort = unsuscribeEmployeeInputPort;
        this.getAllSuscribersInputPort = getAllSuscribersInputPort;
    }
    
    @PostMapping("/suscribe/{type}")
    public void suscribeEmployee (
            @PathVariable String type,
            @RequestParam String email){
        this.suscribeEmployeeInputPort.suscribeEmployee(type, email);
    }
    
    @PostMapping("/unsuscribe/{type}")
    public void unsuscribeEmployee (
            @PathVariable String type,
            @RequestParam String email){
        this.unsuscribeEmployeeInputPort.unsuscribeEmployee(type, email);
    }
    
    @PostMapping("/allsuscribers/{type}")
    public List<SuscriberResponse> allSuscribers(@PathVariable String type){
        return this.getAllSuscribersInputPort.getAllSuscribers(type)
                .stream()
                .map(SuscriberResponse::new)
                .collect(Collectors.toList());
    }

}
