package org.jun.saemangeum.pipeline.domain;

import org.jun.saemangeum.pipeline.application.alarm.AlarmBuilder;

public interface Alarm {
    void sendAlarm(AlarmBuilder alarmBuilder, Object... args);
}
