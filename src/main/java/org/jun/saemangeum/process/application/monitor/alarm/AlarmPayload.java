package org.jun.saemangeum.process.application.monitor.alarm;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class AlarmPayload {
    private final String threadName;
    private final AlarmProcess process;
    private final AlarmType alarmType;
    private final AlarmMessage alarmMessage;
    private final Instant timestamp;
}
