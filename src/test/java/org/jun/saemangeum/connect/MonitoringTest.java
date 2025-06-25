package org.jun.saemangeum.connect;

import org.jun.saemangeum.pipeline.domain.entity.AlarmMessage;
import org.jun.saemangeum.pipeline.domain.entity.AlarmPayload;
import org.jun.saemangeum.pipeline.application.alarm.AlarmProcess;
import org.jun.saemangeum.pipeline.domain.entity.AlarmType;
import org.jun.saemangeum.pipeline.presentation.DiscordMessageAlarm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
public class MonitoringTest {

    @Autowired
    private DiscordMessageAlarm discordMessageAlarm;

    @Test
    @DisplayName("임시 디스코드 메세지 송신 테스트")
    void test() {
        discordMessageAlarm.sendAlarm(() -> AlarmPayload.builder()
                        .process(AlarmProcess.COLLECT)
                        .alarmType(AlarmType.SUCCESS)
                        .alarmMessage(AlarmMessage.COLLECT)
                        .threadName("Test Thread_1")
                        .timestamp(Instant.now())
                        .build(),
                128);
    }
}
