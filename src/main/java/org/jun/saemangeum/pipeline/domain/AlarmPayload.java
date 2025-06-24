package org.jun.saemangeum.pipeline.domain;

import lombok.Builder;
import lombok.Getter;
import org.jun.saemangeum.pipeline.application.alarm.AlarmProcess;

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
