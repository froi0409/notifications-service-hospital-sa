package com.sa.notifications.notification.infrastructure.inputadapters.restapi;
import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.notification.domain.Notification;
import com.sa.notifications.notification.infrastructure.inputadapters.restapi.SuscribeNotificationControllerAdapter;
import com.sa.notifications.notification.infrastructure.inputadapters.restapi.response.SuscriberResponse;
import com.sa.notifications.notification.infrastructure.inputports.restapi.GetAllSuscribersInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.SuscribeEmployeeInputPort;
import com.sa.notifications.notification.infrastructure.inputports.restapi.UnsuscribeEmployeeInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class SuscribeNotificationControllerAdapterTest {
    @Mock
    private SuscribeEmployeeInputPort suscribeEmployeeInputPort;

    @Mock
    private UnsuscribeEmployeeInputPort unsuscribeEmployeeInputPort;

    @Mock
    private GetAllSuscribersInputPort getAllSuscribersInputPort;

    @InjectMocks
    private SuscribeNotificationControllerAdapter suscribeNotificationControllerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuscribeEmployee() {
        String type = "Email";
        String email = "test@example.com";

        // Act
        suscribeNotificationControllerAdapter.suscribeEmployee(type, email);

        // Verify that the suscribe method was called with correct parameters
        verify(suscribeEmployeeInputPort, times(1)).suscribeEmployee(type, email);
    }

    @Test
    void testUnsuscribeEmployee() {
        String type = "Email";
        String email = "test@example.com";

        // Act
        suscribeNotificationControllerAdapter.unsuscribeEmployee(type, email);

        // Verify that the unsuscribe method was called with correct parameters
        verify(unsuscribeEmployeeInputPort, times(1)).unsuscribeEmployee(type, email);
    }

    @Test
    void testAllSuscribers() {
        String type = "Email";

        // Crear un objeto simulado de Notification
        Notification mockNotification = Notification.builder()
                .id(UUID.randomUUID())
                .type("SomeType")
                .build();

        // Crear objetos simulados de EmployeeNotification
        EmployeeNotification mockSubscriber1 = EmployeeNotification.builder()
                .id(UUID.randomUUID())
                .notification(mockNotification)
                .emailEmployee("test1@example.com")
                .build();

        EmployeeNotification mockSubscriber2 = EmployeeNotification.builder()
                .id(UUID.randomUUID())
                .notification(mockNotification)
                .emailEmployee("test2@example.com")
                .build();

        // Crear una lista simulada de suscriptores
        List<EmployeeNotification> mockSuscribers = List.of(mockSubscriber1, mockSubscriber2);

        // Simular la respuesta del servicio de obtención de suscriptores
        when(getAllSuscribersInputPort.getAllSuscribers(type)).thenReturn(mockSuscribers);

        // Obtener la respuesta del controlador
        List<SuscriberResponse> response = suscribeNotificationControllerAdapter.allSuscribers(type);

        // Verificar que se llamó al método de obtener suscriptores
        verify(getAllSuscribersInputPort, times(1)).getAllSuscribers(type);

        // Verificar que la respuesta no sea nula y tenga elementos
        assertNotNull(response);
        assertEquals(mockSuscribers.size(), response.size());

        // Verificar que el contenido de la respuesta sea correcto
        for (int i = 0; i < mockSuscribers.size(); i++) {
            // Comprobar que SuscriberResponse tenga un constructor que acepte EmployeeNotification
            assertEquals(mockSuscribers.get(i).getEmailEmployee(), response.get(i).getEmailEmployee());
        }
    }
}
