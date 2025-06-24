package org.jun.saemangeum.pipeline.application.dto;

import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.CollectSource;

public record RefinedDataDTO(
        String title,
        String position,
        Category category,
        String image,
        String introduction,
        String url,
        CollectSource collectSource
) {
    public static RefinedDataDTO to(Event event, String url, CollectSource collectSource) {
        return new RefinedDataDTO(
                event.getName(),
                event.getLocation(),
                Category.EVENT,
                null,
                event.getDescription(),
                url,
                collectSource
        );
    }

    public static RefinedDataDTO to(Festival festival, String url, CollectSource collectSource) {
        return new RefinedDataDTO(
                festival.getName(),
                festival.getLocation(),
                Category.FESTIVAL,
                null,
                festival.getDescription(),
                url,
                collectSource
        );
    }

    public static RefinedDataDTO to(GunsanCulture gunsanCulture, String url, CollectSource collectSource) {
        return new RefinedDataDTO(
                gunsanCulture.getName(),
                gunsanCulture.getPosition(),
                Category.CULTURE,
                null,
                null,
                url,
                collectSource
        );
    }

    public static RefinedDataDTO to(GimjeCulture gimjeCulture, String url, CollectSource collectSource) {
        return new RefinedDataDTO(
                gimjeCulture.getName(),
                gimjeCulture.getPosition(),
                Category.CULTURE,
                null,
                null,
                url,
                collectSource
        );
    }
}
