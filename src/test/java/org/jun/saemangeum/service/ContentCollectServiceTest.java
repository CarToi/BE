package org.jun.saemangeum.service;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.pipeline.application.schedule.PipelineScheduler;
import org.jun.saemangeum.pipeline.application.service.PipelineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

// 테스트용 h2는 임베디드가 아니라 인메모리로 세팅해야겠네
@Slf4j
@SpringBootTest
public class ContentCollectServiceTest {

    @Autowired
    private PipelineScheduler pipelineScheduler;

    @Autowired
    private ContentRepository contentRepository;

    @Test
    @DisplayName("파이프라인 임시 동작 시도")
    void test() {
        try {
            pipelineScheduler.process();
        } catch (BadSqlGrammarException e) {
            // H2와 MySQL 문법차이로 인한 어쩔 수 없는 발생 이슈
            log.error("H2와 MySQL 문법차이로 인한 어쩔 수 없는 발생 이슈");
        }
        log.info("수집된 데이터 개수 : {}", contentRepository.findAll().size());
    }
}
