package org.jun.saemangeum.process.application.service;

import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;

import java.util.List;

public interface Collector {
    List<RefinedDataDTO> collectData();
}
