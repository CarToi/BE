package org.jun.saemangeum.process.application.collect.base;

import org.jun.saemangeum.global.persistence.domain.Content;

import java.util.List;

public interface Refiner {
    List<Content> refine();
}
