package org.jun.saemangeum.process.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GunsanCulture {
    @JsonProperty("구분")
    private String category;

    @JsonProperty("시설명")
    private String name;

    @JsonProperty("도로명주소")
    private String position;

    @JsonProperty("전화번호")
    private String phone;
}
