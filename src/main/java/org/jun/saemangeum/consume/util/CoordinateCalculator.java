package org.jun.saemangeum.consume.util;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.Coordinate;
import org.jun.saemangeum.consume.domain.dto.KakaoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class CoordinateCalculator {

    @Value("${kakao.centralX}")
    private double centralX;

    @Value("${kakao.centralY}")
    private double centralY;

    @Value("${kakao.distance}")
    private double distance;

    private final RestTemplate coordinateTemplate;

    public Coordinate getCoordinate(String position) {
        String url = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", position)
                .toUriString();

        ResponseEntity<KakaoResponse> response =
                coordinateTemplate.getForEntity(url, KakaoResponse.class);

        KakaoResponse body = response.getBody();

        if (body.getDocuments() == null || body.getDocuments().isEmpty()) {
            return null;
        }

        var doc = body.getDocuments().getFirst();

        if (calculate(Double.parseDouble(doc.getX()), Double.parseDouble(doc.getY())))
            return new Coordinate(Double.parseDouble(doc.getX()), Double.parseDouble(doc.getY()));

        return null;
    }

    private boolean calculate(double x, double y) {
        double dx = x - centralX;
        double dy = y - centralY;

        return distance >= Math.sqrt(dx * dx + dy * dy);
    }
}
