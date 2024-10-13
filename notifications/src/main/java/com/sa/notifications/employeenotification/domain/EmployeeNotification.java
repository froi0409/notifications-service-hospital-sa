package com.sa.notifications.employeenotification.domain;

import com.sa.notifications.common.DomainEntity;
import com.sa.notifications.notification.domain.Notification;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@DomainEntity
public class EmployeeNotification {
    private UUID id;
    private Notification notification;
    private String emailEmployee;
}

