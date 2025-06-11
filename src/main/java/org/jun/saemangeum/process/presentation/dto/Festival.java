package org.jun.saemangeum.process.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;

@Getter
public class Festival {
    @JsonProperty("축제명")
    private String name;

    @JsonProperty("축제시작일")
    private String startDate;

    @JsonProperty("축제종료일")
    private String endDate;

    @JsonProperty("장소(지역)")
    private String location;

    @JsonProperty("주관기관")
    private String organizer;

    @JsonProperty("행사내용")
    private String description;

    @JsonProperty("전화번호")
    private String phone;

    @JsonProperty("비고")
    private String note;
}
