package com.sa.notifications.employeenotification.infrastructure.outputadapters.db;

import com.sa.notifications.employeenotification.domain.EmployeeNotification;
import com.sa.notifications.notification.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeNotificationDbOutputAdapterTest {

    @Mock
    private EmployeeNotificationDbEntityRepository employeeNotificationDbEntityRepository;

    @InjectMocks
    private EmployeeNotificationDbOutputAdapter employeeNotificationDbOutputAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindEmployeeNotificationByEmailAndEmployee_Found() {
        // Given
        String email = "test@example.com";
        Notification notification = Notification.builder().id(UUID.randomUUID()).build();
        EmployeeNotificationDbEntity dbEntity = mock(EmployeeNotificationDbEntity.class);
        when(employeeNotificationDbEntityRepository.findByNotificationIdAndEmailEmployee(notification.getId().toString(), email))
                .thenReturn(Optional.of(dbEntity));
        when(dbEntity.toDomainModel()).thenReturn(EmployeeNotification.builder().emailEmployee(email).build());

        // When
        EmployeeNotification result = employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(notification, email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmailEmployee());
    }

    @Test
    void testFindEmployeeNotificationByEmailAndEmployee_NotFound() {
        // Given
        String email = "test@example.com";
        Notification notification = Notification.builder().id(UUID.randomUUID()).build();
        when(employeeNotificationDbEntityRepository.findByNotificationIdAndEmailEmployee(notification.getId().toString(), email))
                .thenReturn(Optional.empty());

        // When
        EmployeeNotification result = employeeNotificationDbOutputAdapter.FindEmployeeNotificationByEmailAndEmployee(notification, email);

        // Then
        assertNull(result);
    }


    @Test
    void testUnsuscribeEmployee() {
        // Given
        EmployeeNotification employeeNotification = EmployeeNotification.builder()
                .notification(Notification.builder().id(UUID.randomUUID()).build())
                .emailEmployee("test@example.com")
                .build();
        EmployeeNotificationDbEntity dbEntity = mock(EmployeeNotificationDbEntity.class);
        when(employeeNotificationDbEntityRepository.findByNotificationIdAndEmailEmployee(
                employeeNotification.getNotification().getType(),
                employeeNotification.getEmailEmployee())).thenReturn(Optional.of(dbEntity));

        // When
        employeeNotificationDbOutputAdapter.unsuscribeEmployee(employeeNotification);

        // Then
        verify(employeeNotificationDbEntityRepository, times(1)).delete(dbEntity);
    }

    @Test
    void testFindEmployeeNotificationByNotificationId() {
        // Given
        Notification notification = Notification.builder().id(UUID.randomUUID()).build();
        EmployeeNotificationDbEntity dbEntity = mock(EmployeeNotificationDbEntity.class);
        when(employeeNotificationDbEntityRepository.findByNotificationId(notification.getId().toString()))
                .thenReturn(List.of(dbEntity));
        when(dbEntity.toDomainModel()).thenReturn(EmployeeNotification.builder().emailEmployee("test@example.com").build());

        // When
        List<EmployeeNotification> result = employeeNotificationDbOutputAdapter.findEmployeeNotificationByNotificationId(notification);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("test@example.com", result.get(0).getEmailEmployee());
    }
}
