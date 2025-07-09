package org.jun.saemangeum.connect;

import org.jun.saemangeum.consume.domain.dto.Coordinate;
import org.jun.saemangeum.consume.util.CoordinateCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CoordinateTest {

    @Autowired
    private CoordinateCalculator coordinateCalculator;

    @Test
    @DisplayName("카카오 API 위도 & 경도 환산 테스트")
    void test() {
        String position = "전북특별자치도 군산시 영명길 29";
        String wrongPosition = "아리울 예술창고";

        Coordinate coordinate1 = coordinateCalculator.getCoordinate(position);
        Coordinate coordinate2 = coordinateCalculator.getCoordinate(wrongPosition);

        System.out.println(coordinate1);
        System.out.println(coordinate2);

        Assertions.assertNotNull(coordinate1);
        Assertions.assertNull(coordinate2);
    }

}
