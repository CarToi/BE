package org.jun.saemangeum.process.application.collect.base;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.CollectSource;

import java.util.List;

public interface Refiner {
    List<Content> refine();
}
