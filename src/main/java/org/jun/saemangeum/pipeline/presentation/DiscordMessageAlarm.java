package org.jun.saemangeum.pipeline.presentation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jun.saemangeum.pipeline.domain.Alarm;
import org.jun.saemangeum.pipeline.application.alarm.AlarmBuilder;
import org.jun.saemangeum.pipeline.domain.AlarmPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    public void sendAlarm(AlarmBuilder alarmBuilder, Object... args) {
        AlarmPayload payload = alarmBuilder.build();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(payload.getProcess().getProcess() + " - " + payload.getThreadName())
                .setDescription(payload.getAlarmMessage().format(args))
                .setColor(payload.getAlarmType().getColor())
                .setFooter("현재 시각", null)
                .setTimestamp(payload.getTimestamp());

        textChannel.sendMessageEmbeds(embed.build()).queue();
    }
}
