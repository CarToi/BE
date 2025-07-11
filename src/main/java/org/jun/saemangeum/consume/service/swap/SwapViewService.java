package org.jun.saemangeum.consume.service.swap;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.swap.ContentView;
import org.jun.saemangeum.consume.domain.swap.VectorView;
import org.jun.saemangeum.consume.repository.swap.ContentViewRepository;
import org.jun.saemangeum.consume.repository.swap.VectorViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SwapViewService {

    private final ContentViewRepository contentViewRepository;
    private final VectorViewRepository vectorViewRepository;

    public List<VectorView> getVectorViews() {
        return vectorViewRepository.findAllWithContentView();
    }

    public List<ContentView> getContentViewsByClientId(String clientId) {
        return contentViewRepository.findRecommendedContentViewsByClientId(clientId);
    }
}
