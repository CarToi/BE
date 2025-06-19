package org.jun.saemangeum.process.application.monitor.alarm;

import lombok.Getter;

@Getter
public enum AlarmProcess {
    COLLECT("데이터 수집"),
    EMBEDDING("벡터 임베딩 전처리"),
    RETRY("임베딩 재시도");

    private final String process;

    AlarmProcess(String process) {
        this.process = process;
    }
}
