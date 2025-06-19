package org.jun.saemangeum.process.application.monitor;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;

@Component
public class DiscordMessageAlarm implements Alarm {

    private final TextChannel textChannel;

    public DiscordMessageAlarm(
            JDA jda, @Value("${discord.channel}") String channel) {
        if (jda == null) {
            throw new IllegalStateException("jda 설정에 오류가 있습니다");
        }

        this.textChannel = jda.getTextChannelById(channel);

        if (this.textChannel == null) {
            throw new IllegalStateException("디스코드 채널을 찾을 수 없습니다. 채널 ID 확인 필요");
        }
    }

    @Override
    public void sendAlarm() {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("📦 수집 완료 (" + "테스트 스레드" + ")")
                .setDescription("> Saemangeum API에서 데이터를 수집했습니다.")
                .addField("수집기", "`" + "테스트 수집기" + "`", false)
                .addField("총 수집", "`" + "count" + "건`", false)
                .setColor(Color.GREEN)
                .setFooter("Saemangeum DataBot", null)
                .setTimestamp(Instant.now());

        textChannel.sendMessageEmbeds(embed.build()).queue();
    }
}
