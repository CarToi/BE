package org.jun.saemangeum.process.domain.dto;

import org.jun.saemangeum.global.domain.Category;

public record RefinedDataDTO(
        String title,
        String position,
        Category category,
        String image,
        String introduction
) {
}
