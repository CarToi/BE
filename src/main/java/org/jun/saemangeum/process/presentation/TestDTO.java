package org.jun.saemangeum.process.presentation;

import org.jun.saemangeum.global.domain.Content;

public record TestDTO(
        String title,
        String position,
        String img,
        String introduction,
        String url
) {
    public static TestDTO of(Content content) {
        return new TestDTO(content.getTitle(), content.getPosition(), content.getImage(), content.getIntroduction(), content.getUrl());
    }
}
