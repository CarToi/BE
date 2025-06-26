package org.jun.saemangeum.pipeline.application.alarm;

import org.jun.saemangeum.pipeline.domain.entity.AlarmPayload;

@FunctionalInterface
public interface AlarmBuilder {
    AlarmPayload build();
}
