package org.jun.saemangeum.global.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.repository.VectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorService {

    private final VectorRepository vectorRepository;

    @Transactional
    public void saveVectors(List<Vector> vectors) {
        vectorRepository.saveAll(vectors);
    }

    @Transactional
    public void saveVector(Vector vector) {
        vectorRepository.save(vector);
    }

    @Transactional
    public void deleteAll() {
        vectorRepository.deleteAll();
    }

    // 캐싱 대상
    @Transactional(readOnly = true)
    public List<Vector> getVectors() {
        return vectorRepository.findAllWithContent();
    }
}
