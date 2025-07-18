package org.jun.saemangeum.service;

import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.pipeline.application.service.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 테스트용 h2는 임베디드가 아니라 인메모리로 세팅해야겠네
@SpringBootTest
public class ContentCollectServiceTest {

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    private ContentService contentService;

//    @Test
//    @DisplayName("가상 스레드풀 기반 데이터 저장 확인용 단위 테스트")
//    void testContentCollectServiceByVirtualThread() {
//        contentDataProcessService.collectAndSaveAsync().join(); // 비동기식 저장이므로 저장 끝날때까지 기다리기
//        List<Content> contents = contentService.getContents();
//
//        // 현재까지 수집한 데이터들이 210개긴한데 얘는 나중에 폐기해야 할듯
//        Assertions.assertTrue(contents.size() < 210, "제목 필터링으로 예상치보다 적어질 것");
//        Assertions.assertEquals(contents.getFirst().getClass(), Content.class);
//    }
}
