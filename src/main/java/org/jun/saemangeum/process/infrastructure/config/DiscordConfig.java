package org.jun.saemangeum.process.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DiscordConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda() {
        JDA jda;

        try {
            jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.customStatus("열심히 지켜보는 중..."))
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            jda = null;
            log.error("디스코드 설정 실패");
        }

        return jda;
    }
}
