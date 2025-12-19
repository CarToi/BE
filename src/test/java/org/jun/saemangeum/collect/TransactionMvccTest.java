package org.jun.saemangeum.collect;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.config.Initializer;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.application.service.EmbeddingVectorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 해당 클래스에서만 임시 MySQL로 테스트
 */
@Slf4j
@SpringBootTest
public class TransactionMvccTest {

    @Autowired
    private EmbeddingVectorService embeddingVectorService;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockitoBean
    Initializer initializer;

    private static final String TITLE = "제목";
    private static final String POSITION = "어딘가";
    private static final Category CATEGORY = Category.CULTURE;
    private static final String INTRODUCTION = "옛날 소개입니다";
    private static final String NEW_INTRODUCTION = "새로운 소개입니다";

    @BeforeEach
    void setUp() {
        RefinedDataDTO dto = new RefinedDataDTO(
                TITLE, POSITION, CATEGORY, "img", INTRODUCTION, "url", CollectSource.BATOCR
        );
        byte[] bytes = new byte[]{};
        Content content = contentRepository.save(Content.create(dto));
        content.updateVector(Vector.builder().content(content).vector(bytes).build());
    }

    @Test
    @DisplayName("MySQL 기반 트랜잭션 MVCC 전략 스냅샷 구 데이터 조회 테스트")
    void test() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);
        AtomicReference<String> read = new AtomicReference<>();

        RefinedDataDTO dto = new RefinedDataDTO(
                TITLE, POSITION, CATEGORY, "img", NEW_INTRODUCTION, "url", CollectSource.BATOCR);
        byte[] bytes = new byte[]{};

        // 트랜잭션 1) 기존 데이터 읽기
        pool.submit(() -> {
            try {
                TransactionTemplate template = new TransactionTemplate(transactionManager);
                template.setReadOnly(true); // 읽기 전용으로 MVCC 스냅샷 확보
                template.execute(status -> {
                    try {
                        startLatch.await();
                        Content content = contentRepository.findByTitle(TITLE).orElseThrow();
                        read.set(content.getIntroduction());
                        log.info("[TX1] 읽은 소개: {}", content.getIntroduction());
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    return null;
                });
                doneLatch.countDown();
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                doneLatch.countDown();
            }
        });

        // 트랜잭션 2) 데이터 업데이트
        pool.submit(() -> {
            TransactionTemplate template = new TransactionTemplate(transactionManager);
            template.execute(status -> {
                try {
                    startLatch.await();
                    embeddingVectorService.upsertContent(dto, bytes);
                    log.info("[TX2] 업데이트 완료");
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                return null;
            });
            doneLatch.countDown();
        });

        // 동시 시작
        startLatch.countDown();
        doneLatch.await();

        // 최종
        Content contentState = contentRepository.findByTitle(TITLE).orElseThrow();
        log.info("[FINAL] 최종 소개: {}", contentState.getIntroduction());

        // result
        Assertions.assertEquals(INTRODUCTION, read.get()); // TX1에서 읽은 값은 아직 변경되지 않았음
        Assertions.assertEquals(NEW_INTRODUCTION, contentState.getIntroduction()); // 최종 DB 값은 업데이트됨
    }
}
