package org.jun.saemangeum.benchmark.process;

import org.jun.saemangeum.pipeline.application.service.ContentCollectService;
import org.openjdk.jmh.annotations.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
@Fork(1)
public class ContentCollectServiceBenchmark {

    private ContentCollectService contentCollectService;

    @Setup(Level.Trial)
    public void setUp() {
        var context = new AnnotationConfigApplicationContext(org.jun.saemangeum.benchmark.process.ContentCollectServiceBenchmark.TestConfig.class);
        contentCollectService = context.getBean(ContentCollectService.class);
    }

    @Configuration
    @ComponentScan(basePackages = "org.jun.saemangeum")
    @PropertySource("classpath:application.yml")
    static class TestConfig {
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
        contentCollectService.collectAndSaveAsync().join();
    }
}
