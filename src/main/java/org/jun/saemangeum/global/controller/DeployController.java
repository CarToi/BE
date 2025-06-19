package org.jun.saemangeum.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
public class DeployController {

    @Value("${server.env}")
    private String env;

    @GetMapping
    public String env() {
        return env;
    }
}
