package org.jun.saemangeum.process.application.collect.base;

// 기본 Supplier가 예외를 안 던지기 때문에 직접 던지는 함수형 인터페이스 작성
@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
