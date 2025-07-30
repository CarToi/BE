package org.jun.saemangeum.global.controller;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.pipeline.application.schedule.PipelineScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
public class DeployController {

    private final PipelineScheduler scheduler;

    @Value("${server.env}")
    private String env;

    @GetMapping
    public String env() {
        return env;
    }

    // @GetMapping("/test")
    // public String test() {
    //     scheduler.process();
    //     return "start";
    // }
}
