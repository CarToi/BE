package org.jun.saemangeum.process.application.collect.crawl;

import lombok.Getter;

import java.util.Map;

@Getter
public enum City {
    GUNSAN(Map.of("gunsan", "2009163854184")),
    GIMJE(Map.of("gimje", "2009163855072")),
    BUAN(Map.of("buan", "2009163855699"));

    private final Map<String, String> city;

    City(Map<String, String> city) {
        this.city = city;
    }
}
