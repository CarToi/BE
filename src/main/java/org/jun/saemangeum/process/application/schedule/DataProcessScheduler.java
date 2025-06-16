package org.jun.saemangeum.process.application.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.process.application.service.ContentDataProcessService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessScheduler {

    private final ContentDataProcessService contentDataProcessService;

    @Scheduled(cron = "0 0 3 ? * SUN")
    public void process() {
        log.info("[스케줄러] 콘텐츠 수집 및 전처리 프로세스 시작");
        contentDataProcessService.collectAndSaveAsync();
    }
}
