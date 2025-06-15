package org.jun.saemangeum.process.application.collect.base;

import org.jun.saemangeum.global.domain.Content;

import java.util.List;

public interface Refiner {
    List<Content> refine();

    // 얘를 하위 손자 구현체들에서 데이터 업데이트 로직이 필요한지 확인하는 걸로 쓰면 되지 않을까
    // 업데이트가 없다면 빈 리스트를 반환한다
//    boolean isNeedToUpdate();
}
