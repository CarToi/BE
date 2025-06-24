package org.jun.saemangeum.pipeline.domain;

import lombok.Getter;

import java.awt.*;

@Getter
public enum AlarmType {
    SUCCESS(Color.GREEN),
    WARNING(Color.YELLOW),
    ERROR(Color.RED);

    private final Color color;

    AlarmType(Color color) {
        this.color = color;
    }
}
