package org.jun.saemangeum.pipeline.application.monitor.alarm;

@FunctionalInterface
public interface AlarmBuilder {
    AlarmPayload build();
}
