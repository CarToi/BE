package org.jun.saemangeum.process.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GimjeCulture {
    @JsonProperty("수용인원")
    private String capacity;

    @JsonProperty("시 설 명")
    private String name;

    @JsonProperty("연락처")
    private String phone;

    @JsonProperty("연번")
    private int index;

    @JsonProperty("주    소")
    private String position;
}
