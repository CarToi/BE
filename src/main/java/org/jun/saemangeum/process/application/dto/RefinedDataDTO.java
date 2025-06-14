package org.jun.saemangeum.process.application.dto;

import org.jun.saemangeum.global.domain.Category;

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

    public static RefinedDataDTO to(GunsanCulture gunsanCulture, String url) {
        return new RefinedDataDTO(
                gunsanCulture.getName(),
                gunsanCulture.getPosition(),
                Category.CULTURE,
                null,
                null,
                url
        );
    }

    public static RefinedDataDTO to(GimjeCulture gimjeCulture, String url) {
        return new RefinedDataDTO(
                gimjeCulture.getName(),
                gimjeCulture.getPosition(),
                Category.CULTURE,
                null,
                null,
                url
        );
    }
}
