package org.jun.saemangeum.process.application.monitor.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final List<Alarm> alarms;

    public void sendCollectSuccess(String threadName, int size) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.COLLECT)
                        .alarmType(AlarmType.SUCCESS)
                        .alarmMessage(AlarmMessage.COLLECT)
                        .threadName(threadName)
                        .timestamp(Instant.now())
                        .build(), size));
    }

    public void sendCollectError(String threadName, Exception e) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.COLLECT)
                        .alarmType(AlarmType.ERROR)
                        .alarmMessage(AlarmMessage.ERROR)
                        .threadName(threadName)
                        .timestamp(Instant.now())
                        .build(), e.getClass().getSimpleName()));
    }

    public void sendUnchanged(String flowName) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.COLLECT)
                        .alarmType(AlarmType.SUCCESS)
                        .alarmMessage(AlarmMessage.UNCHANGED)
                        .threadName(flowName)
                        .timestamp(Instant.now())
                        .build()));
    }

    public void sendEmbeddingSuccess(String flowName, int count) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.EMBEDDING)
                        .alarmType(AlarmType.SUCCESS)
                        .alarmMessage(AlarmMessage.EMBEDDING)
                        .threadName(flowName)
                        .timestamp(Instant.now())
                        .build(), count));
    }

    public void sendEmbeddingUnchanged(String flowName) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.EMBEDDING)
                        .alarmType(AlarmType.SUCCESS)
                        .alarmMessage(AlarmMessage.UNCHANGED)
                        .threadName(flowName)
                        .timestamp(Instant.now())
                        .build()));
    }

    public void sendEmbeddingError(String flowName, String errorType) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.EMBEDDING)
                        .alarmType(AlarmType.ERROR)
                        .alarmMessage(AlarmMessage.ERROR)
                        .threadName(flowName)
                        .timestamp(Instant.now())
                        .build(), errorType));
    }

    public void sendRetrySuccess(String flowName) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.RETRY)
                        .alarmType(AlarmType.SUCCESS)
                        .alarmMessage(AlarmMessage.RETRY)
                        .threadName(flowName)
                        .timestamp(Instant.now())
                        .build()));
    }

    public void sendRetryError(String flowName, String errorType) {
        alarms.forEach(alarm -> alarm.sendAlarm(
                () -> AlarmPayload.builder()
                        .process(AlarmProcess.RETRY)
                        .alarmType(AlarmType.ERROR)
                        .alarmMessage(AlarmMessage.ERROR)
                        .threadName(flowName)
                        .timestamp(Instant.now())
                        .build(), errorType));
    }
}
