package org.jun.saemangeum.service;

import org.jun.saemangeum.global.domain.Content;
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

    @Test
    @DisplayName("서비스 일괄 전처리 및 저장 확인용 단위 테스트")
    void testContentCollectService() {
        contentCollectService.collectAndSave();
        List<Content> contents = contentCollectService.getContents();

        // 현재까지 수집한 데이터들이 157개긴한데 얘는 나중에 폐기해야 할듯
        Assertions.assertEquals(contents.size(), 157);
        Assertions.assertEquals(contents.getFirst().getClass(), Content.class);
    }
}
