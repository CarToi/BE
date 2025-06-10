package org.jun.saemangeum.benchmark.process;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.process.application.collect.base.Refiner;
import org.jun.saemangeum.process.application.service.ContentCollectService;
import org.jun.saemangeum.global.service.ContentService;
import org.mockito.Mockito;
import org.openjdk.jmh.annotations.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(1)
public class ContentCollectServiceBenchmark {

    private ContentCollectService contentCollectService;

    @Configuration
    @ComponentScan(basePackages = "org.jun.saemangeum")
    static class TestConfig {
    }

    /**
     * 외부 소스 의존성 모킹처리
     */
    @Setup
    public void setup() {
        ContentRepository mockRepo = Mockito.mock(ContentRepository.class);
        // 실제 저장 없이 전달된 리스트 파라미터 그대로 반환하도록 모킹 처리
        Mockito.when(mockRepo.saveAll(Mockito.anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Refiner마다 고유의 지연 시간 (ms)을 지정
        int[] contentCounts = {
                40, // 공공데이터 기반 축제 정보
                51, // 공공데이터 기반 공연행사 정보
                9,  // 새만금개발청 방조제 관광지
                41, // 새만금개발청 인접도시 관광지
                6,  // 새만금개발공사 군도 관광지
                10  // 군산시 문화관광 축제행사 정보
        };

        int[] simulatedDelays = {
                300, // 축제 정보 API 지연
                350, // 공연행사 API 지연
                200, // 방조제 크롤링 지연
                800, // 인접도시 크롤링 지연
                600, // 군도 크롤링 지연
                150  // 군산시 페이지 크롤링 지연
        };

        List<Refiner> mockRefiners = IntStream.range(0, contentCounts.length)
                .mapToObj(i -> {
                    List<Content> mockContent = IntStream.range(0, contentCounts[i])
                            .mapToObj(j -> Mockito.mock(Content.class))
                            .collect(Collectors.toList());

                    Refiner refiner = Mockito.mock(Refiner.class);
                    Mockito.when(refiner.refine()).thenAnswer(invocation -> {
                        try {
                            Thread.sleep(simulatedDelays[i]); // 지연 시뮬레이션
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // 인터럽트 복구
                        }
                        return mockContent;
                    });
                    return refiner;
                })
                .toList();

        contentCollectService = new ContentCollectService(mockRefiners, new ContentService(mockRepo));
    }

    /**
     * 단일 스레드 내 블로킹 테스트
     */
    @Benchmark
    public void benchmarkCollectAndSave() {
        contentCollectService.collectAndSave();
    }

    /**
     * 단일 스레드 내 병렬 스트림 테스트
     */
    @Benchmark
    public void benchmarkCollectAndSaveByParallelStream() {
        contentCollectService.collectAndSaveByParallelStream();
    }

    /**
     * 가상 스레드 기반 프로세스 테스트
     */
    @Benchmark
    public void benchmarkCollectAndSaveAsync() {
        contentCollectService.collectAndSaveAsync();
    }
}