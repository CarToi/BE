package org.jun.saemangeum.process.application.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TitleDuplicateChecker {

    // 스레드 안전 Set 생성
    private final Set<String> seenTitles = ConcurrentHashMap.newKeySet();

    public boolean isDuplicate(String title) {
        // 중복이면 true, 아니면 false
        return !seenTitles.add(title.replaceAll("\\d|\\s", ""));
    }

    public void reset() {
        seenTitles.clear(); // 수집이 끝난 후 초기화 가능
    }
}
