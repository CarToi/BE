package org.jun.saemangeum.process.application.monitor.alarm;

import lombok.Getter;

@Getter
public enum AlarmMessage {
    COLLECT("\uD83D\uDCC4 수집 및 업데이트 : %d 건"),
    EMBEDDING("\uD83D\uDEE0\uFE0F 벡터 임베딩 완료 // 실패 : %d 건"),
    RETRY("♻\uFE0F 벡터 임베딩 재시도 완료"),
    UNCHANGED("\uD83D\uDCA4 업데이트 없음"),
    ERROR("\uD83D\uDEA8 %s 에러 발생!");

    private final String template;

    AlarmMessage(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
