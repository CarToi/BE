package org.jun.saemangeum.process.application.monitor.alarm;

@FunctionalInterface
public interface AlarmBuilder {
    AlarmPayload build();
}
