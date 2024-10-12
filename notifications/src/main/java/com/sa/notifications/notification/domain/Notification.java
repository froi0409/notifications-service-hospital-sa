package com.sa.notifications.notification.domain;

import com.sa.notifications.common.DomainEntity;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@DomainEntity
public class Notification {
    private UUID id;
    private String type;
}

