package org.jun.saemangeum.process.presentation;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
import org.jun.saemangeum.process.application.service.ContentDataProcessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ContentDataProcessService contentDataProcessService;
    private final EmbeddingVectorService embeddingVectorService;

    @GetMapping
    public String testProcess() {
        contentDataProcessService.collectAndSaveAsync();
        return "전처리 완료";
    }

    @GetMapping("/{text}")
    public List<TestDTO> testUseCase(@PathVariable String text) {
        List<Content> contents = embeddingVectorService.calculateSimilarity(text);
        return contents.stream().map(TestDTO::of).toList();
    }
}
