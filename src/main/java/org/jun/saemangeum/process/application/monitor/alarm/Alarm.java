package org.jun.saemangeum.process.application.monitor.alarm;

public interface Alarm {
    void sendAlarm(AlarmBuilder alarmBuilder, Object... args);
}
