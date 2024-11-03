package com.sa.notifications.notification.infrastructure.outputadapters.restapi;

import com.sa.notifications.common.OutputAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.sa.notifications.notification.infrastructure.outputports.restapi.CheckEmailEmployeeOutputPort;

@OutputAdapter
public class NotificationRestApiOutputAdapter implements CheckEmailEmployeeOutputPort{
    
    private final RestTemplate restTemplate;

    public NotificationRestApiOutputAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean checkEmailEmployee(String email) {
        /*
        String url = "lb://EMPLOYEES/v1/employees/email/" + email;
        try {
            restTemplate.headForHeaders(url);
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            } else {
                throw e;
            }
        }
        */
        
        return true;
    }

}
