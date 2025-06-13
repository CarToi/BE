package org.jun.saemangeum.service;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.process.application.service.ContentCollectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ContentCollectServiceTest {

    @Autowired
    private ContentCollectService contentCollectService;

    @Autowired
    private ContentService contentService;

    @Test
    @DisplayName("서비스 일괄 전처리 및 저장 확인용 단위 테스트")
    void testContentCollectService() {
        contentCollectService.collectAndSave();
        List<Content> contents = contentService.getContents();

        // 현재까지 수집한 데이터들이 157개긴한데 얘는 나중에 폐기해야 할듯
        Assertions.assertTrue(contents.size() < 157, "제목 필터링으로 예상치보다 적어질 것");
        Assertions.assertEquals(contents.getFirst().getClass(), Content.class);
    }

    @Test
    @DisplayName("가상 스레드풀 기반 데이터 저장 확인용 단위 테스트")
    void testContentCollectServiceByVirtualThread() {
        contentCollectService.collectAndSaveAsync().join(); // 비동기식 저장이므로 저장 끝날때까지 기다리기
        List<Content> contents = contentService.getContents();

        // 현재까지 수집한 데이터들이 157개긴한데 얘는 나중에 폐기해야 할듯
        Assertions.assertTrue(contents.size() < 157, "제목 필터링으로 예상치보다 적어질 것");
        Assertions.assertEquals(contents.getFirst().getClass(), Content.class);
    }
}
