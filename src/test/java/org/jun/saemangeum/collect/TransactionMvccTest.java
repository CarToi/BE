package org.jun.saemangeum.collect;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.config.Initializer;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.global.repository.VectorRepository;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.application.service.EmbeddingVectorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class TransactionMvccTest {

    @Autowired
    private EmbeddingVectorService embeddingVectorService;

    @Autowired
    private ContentRepository contentRepository;

    @MockitoBean
    Initializer initializer;

    private static final String TITLE = "제목";
    private static final String POSITION = "어딘가";
    private static final Category CATEGORY = Category.CULTURE;
    private static final String INTRODUCTION = "옛날 소개입니다";
    private static final String NEW_INTRODUCTION = "새로운 소개입니다";
    private String result;
    private CountDownLatch latch = new CountDownLatch(1);

    @BeforeEach
    void setUp() {
        RefinedDataDTO dto = new RefinedDataDTO(
                TITLE, POSITION, CATEGORY, "img", INTRODUCTION, "url", CollectSource.BATOCR
        );
        byte[] bytes = new byte[]{};
        Content content = contentRepository.save(Content.create(dto));
        content.upsertVector(bytes);
    }

    @Test
    @DisplayName("MySQL 기반 트랜잭션 MVCC 전략 스냅샷 구 데이터 조회 테스트")
    void test() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        RefinedDataDTO dto = new RefinedDataDTO(
                TITLE, POSITION, CATEGORY, "img", NEW_INTRODUCTION, "url", CollectSource.BATOCR
        );
        byte[] bytes = new byte[]{};

        pool.submit(() -> {
            embeddingVectorService.upsertContent(dto, bytes);
            latch.countDown();
        });

        log.info("임시 로그 확인 : {}", contentRepository.findByTitle(TITLE).orElseThrow().getIntroduction());
    }
}
