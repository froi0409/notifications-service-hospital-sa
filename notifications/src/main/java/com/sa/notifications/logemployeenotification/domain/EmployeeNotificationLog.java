package com.sa.notifications.logemployeenotification.domain;

import com.sa.notifications.common.DomainEntity;
import com.sa.notifications.lognotification.domain.NotificationLog;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@DomainEntity
public class EmployeeNotificationLog {
    private UUID id;
    private NotificationLog notification;
    private String emailEmployee;
}

