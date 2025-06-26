package org.jun.saemangeum.pipeline.application.collect.base;

import org.jun.saemangeum.global.domain.Content;

import java.util.List;

public interface Refiner {
    List<Content> refine();
}
