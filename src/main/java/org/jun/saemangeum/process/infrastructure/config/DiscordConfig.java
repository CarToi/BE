package org.jun.saemangeum.process.infrastructure.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda() {
        return JDABuilder.createDefault(token)
                .setActivity(Activity.customStatus("열심히 지켜보는 중..."))
                .build();
    }
}
