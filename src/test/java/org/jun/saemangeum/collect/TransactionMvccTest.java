package org.jun.saemangeum.collect;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jun.saemangeum.global.config.Initializer;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.global.repository.VectorRepository;
import org.jun.saemangeum.pipeline.application.service.EmbeddingVectorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class TransactionMvccTest {

    @Autowired
    private EmbeddingVectorService embeddingVectorService;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private VectorRepository vectorRepository;

    @MockitoBean
    Initializer initializer;

    private static final String TITLE = "제목";
    private static final String POSITION = "어딘가";
    private static final Category CATEGORY = Category.CULTURE;
    private static final String INTRODUCTION = "옛날 소개입니다";
    private static final String NEW_INTRODUCTION = "새로운 소개입니다";
    private String result;

    @BeforeEach
    void setUp() {
        Content content = Content.builder()
                .title(TITLE)
                .position(POSITION)
                .category(CATEGORY)
                .introduction(INTRODUCTION)
                .build();
        contentRepository.save(content);
        Vector vector = Vector.builder().build();
        content.setVector(vector);
        vectorRepository.save(vector);
    }

    @Test
    @DisplayName("MySQL 기반 트랜잭션 MVCC 전략 스냅샷 구 데이터 조회 테스트")
    void test() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        Content newContent = Content.builder()
                .title(TITLE)
                .position(POSITION)
                .category(CATEGORY)
                .introduction(NEW_INTRODUCTION)
                .build();
        Vector newVector = Vector.builder().build();

        pool.submit(() -> {
            embeddingVectorService.deleteAndSaveOneTransaction(newContent, newVector);
            latch.countDown();
        });

        pool.submit(() -> {
            while (latch.getCount() > 0) {
                Content sameContent = contentRepository
                        .findByTitle(TITLE)
                        .orElseThrow(IllegalArgumentException::new);

                result = sameContent.getIntroduction();
            }
        });

        Assertions.assertEquals(INTRODUCTION, result);
    }
}
