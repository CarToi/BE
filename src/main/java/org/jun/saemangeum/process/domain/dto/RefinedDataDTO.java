package org.jun.saemangeum.process.domain.dto;

import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.process.presentation.dto.Culture;
import org.jun.saemangeum.process.presentation.dto.Event;
import org.jun.saemangeum.process.presentation.dto.Festival;

public record RefinedDataDTO(
        String title,
        String position,
        Category category,
        String image,
        String introduction,
        String url
) {
    public static RefinedDataDTO to(Event event, String url) {
        return new RefinedDataDTO(
                event.getName(),
                event.getLocation(),
                Category.EVENT,
                null,
                event.getDescription(),
                url
        );
    }

    public static RefinedDataDTO to(Festival festival, String url) {
        return new RefinedDataDTO(
                festival.getName(),
                festival.getLocation(),
                Category.FESTIVAL,
                null,
                festival.getDescription(),
                url
        );
    }

    public static RefinedDataDTO to(Culture culture, String url) {
        return new RefinedDataDTO(
                culture.getName(),
                culture.getPosition(),
                Category.CULTURE,
                null,
                null,
                url
        );
    }
}
