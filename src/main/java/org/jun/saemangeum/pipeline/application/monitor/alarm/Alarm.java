package org.jun.saemangeum.pipeline.application.monitor.alarm;

public interface Alarm {
    void sendAlarm(AlarmBuilder alarmBuilder, Object... args);
}
