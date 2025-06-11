package org.jun.saemangeum.process.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;

@Getter
public class Event {
    @JsonProperty("행사명")
    private String name;

    @JsonProperty("행사시작일")
    private String startDate;

    @JsonProperty("행사종료일")
    private String endDate;

    @JsonProperty("행사 장소")
    private String location;

    @JsonProperty("주최")
    private String organizer;

    @JsonProperty("행사 내용")
    private String description;

    @JsonProperty("문의처")
    private String contact;

    public RefinedDataDTO convertToDTO() {
        return new RefinedDataDTO(
                this.name, this.location, Category.EVENT, null, this.description);
    }
}
