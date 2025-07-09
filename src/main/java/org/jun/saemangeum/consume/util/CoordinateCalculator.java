package org.jun.saemangeum.consume.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.consume.domain.dto.Coordinate;
import org.jun.saemangeum.consume.domain.dto.KakaoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoordinateCalculator {

    @Value("${kakao.apiKey}")
    private String apiKey;

    @Value("${kakao.centralX}")
    private double centralX;

    @Value("${kakao.centralY}")
    private double centralY;

    @Value("${kakao.distance}")
    private double distance;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Coordinate getCoordinate(String position) {
        String responseJson = getResponseJson(position);
        KakaoResponse body = null;

        try {
            body = objectMapper.readValue(responseJson, KakaoResponse.class);
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
        }

        if (body == null) return null;
        if (body.getDocuments() == null || body.getDocuments().isEmpty()) return null;

        var doc = body.getDocuments().getFirst();

        // 최상위 x/y
        if (doc.getX() != null && doc.getY() != null) return toCoordinate(doc.getX(), doc.getY());

        // road_address
        if (doc.getRoad_address() != null && doc.getRoad_address().getX() != null) {
            return toCoordinate(doc.getRoad_address().getX(), doc.getRoad_address().getY());
        }

        // address
        if (doc.getAddress() != null && doc.getAddress().getX() != null) {
            return toCoordinate(doc.getAddress().getX(), doc.getAddress().getY());
        }

        // 최종 폴백
        return null;
    }

    private String getResponseJson(String position) {
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";

        try {
            String encodedPosition = URLEncoder.encode(position, StandardCharsets.UTF_8);
            String urlWithParams = apiUrl + "?query=" + encodedPosition;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithParams))
                    .header("Authorization", "KakaoAK " + apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private Coordinate toCoordinate(String x, String y) {
        double dx = Double.parseDouble(x);
        double dy = Double.parseDouble(y);
        return calculate(dx, dy) ? new Coordinate(dx, dy) : null;
    }

    private boolean calculate(double x, double y) {
        double dx = x - centralX;
        double dy = y - centralY;

        return distance >= Math.sqrt(dx * dx + dy * dy);
    }
}
