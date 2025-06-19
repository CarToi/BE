package org.jun.saemangeum.connect;

import org.jun.saemangeum.process.application.monitor.DiscordMessageAlarm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoringTest {

    @Autowired
    private DiscordMessageAlarm discordMessageAlarm;

    @Test
    @DisplayName("임시 디스코드 메세지 송신 테스트")
    void test() {
        discordMessageAlarm.sendAlarm();
    }
}
