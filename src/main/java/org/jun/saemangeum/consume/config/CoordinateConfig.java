//package org.jun.saemangeum.consume.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class CoordinateConfig {
//
//    @Value("${kakao.apiKey}")
//    private String apiKey;
//
//    @Bean(name = "coordinateTemplate")
//    public RestTemplate coordinateTemplate() {
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(5000);
//        requestFactory.setReadTimeout(5000);
//
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//        // Authorization 헤더 자동 추가용 인터셉터 설정
//        restTemplate.getInterceptors().add((request, body, execution) -> {
//            request.getHeaders().add("Authorization", "KakaoAK " + apiKey);
//            request.getHeaders().add("User-Agent", "Mozilla/5.0");
//            return execution.execute(request, body);
//        });
//
//        return restTemplate;
//    }
//}

// 카카오 인코딩 정책을 먼저 파악하고 빈 등록하기
