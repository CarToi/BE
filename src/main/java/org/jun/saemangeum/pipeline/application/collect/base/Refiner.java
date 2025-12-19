package org.jun.saemangeum.pipeline.application.collect.base;

import org.jun.saemangeum.global.domain.Content;

import java.util.List;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;

public interface Refiner {
    List<RefinedDataDTO> refine();
}
