package org.jun.saemangeum.process.application.service.constant;

import lombok.Getter;

@Getter
public enum FestivalOpenApiPath {
    PATH_2023("/15006172/v1/uddi:ede8925d-bfbd-49fc-9f3c-abf1ead5b402"),
    PATH_2021("/15006172/v1/uddi:68f7b546-df14-4717-8119-f8d20d3a4224"),
    PATH_2019("/15006172/v1/uddi:070933dc-7dcc-4aca-8a3b-882c34de1707_201908211747"),
    PATH_2018("/15006172/v1/uddi:cb44741a-0fe0-4e32-a586-d6494629f864_201908211747"),
    PATH_2017("/15006172/v1/uddi:e423bcec-8f2e-4f55-8f47-de7e750b3256_201908211746"),
    PATH_2015("/15006172/v1/uddi:620eb5f6-fe80-4a58-bcce-f6c60c7326b5_201908211744");

    private final String path;

    FestivalOpenApiPath(String path) {
        this.path = path;
    }
}

