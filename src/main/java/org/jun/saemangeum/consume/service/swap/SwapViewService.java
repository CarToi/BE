package org.jun.saemangeum.consume.service.swap;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.repository.swap.ContentViewRepository;
import org.jun.saemangeum.consume.repository.swap.VectorViewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SwapViewService {

    private final ContentViewRepository contentViewRepository;
    private final VectorViewRepository vectorViewRepository;

}
