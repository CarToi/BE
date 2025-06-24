package org.jun.saemangeum.pipeline.application.alarm;

import org.jun.saemangeum.pipeline.domain.AlarmPayload;

@FunctionalInterface
public interface AlarmBuilder {
    AlarmPayload build();
}
