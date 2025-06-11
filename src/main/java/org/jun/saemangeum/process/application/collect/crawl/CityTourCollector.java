package org.jun.saemangeum.process.application.collect.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.process.application.collect.base.CrawlingCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CityTourCollector extends CrawlingCollector {

    private static final List<Map<String, City>> CITIES = List.of(
            Map.of("군산시", City.GUNSAN),
            Map.of("김제시", City.GIMJE),
            Map.of("부안군", City.BUAN)
    );

    private static final String PATH = "https://www.saemangeum.go.kr/sda/content.do?key=";

    @Override
    public List<RefinedDataDTO> collectData() {
        List<RefinedDataDTO> data = new ArrayList<>();

        for (Map<String, City> cityMap : CITIES) {
            Map.Entry<String, City> entry = cityMap.entrySet().iterator().next();
            Map<String, String> cityAddress = entry.getValue().getCity();
            Map.Entry<String, String> city = cityAddress.entrySet().iterator().next();

            try {
                Document doc = Jsoup.connect(PATH + city.getValue()).timeout(5 * 1000).get();
                Elements items = doc.select("ul.li_spot." + city.getKey() + " > li");

                for (Element item : items) {
                    Element element = item.selectFirst("div.info_spot > h5");
                    if (element == null) continue;

                    String title = element.text().trim();

                    // 설명 <p> 여러 개 있는 경우 모두 합치기
                    Elements descElements = item.select("div.info_spot > p");
                    StringBuilder descBuilder = new StringBuilder();
                    for (Element p : descElements) {
                        if (descBuilder.length() > 300) break;
                        descBuilder.append(p.text().trim()).append("\n");
                    }
                    String introduction = descBuilder.toString().trim();

                    log.info(introduction);

                    // 이미지 URL
                    Element imgEl = item.selectFirst("div.thumb img");
                    String imgSrc = imgEl != null ? "https://www.saemangeum.go.kr" + imgEl.attr("src") : null;

                    // 주소 추출
                    String position = null;
                    Elements dtElements = item.select("div.info_spot dt");
                    for (Element dt : dtElements) {
                        if (dt.text().replaceAll("\\s+", "").equals("주소")) {
                            Element dd = dt.nextElementSibling();
                            if (dd != null && dd.tagName().equals("dd")) {
                                position = dd.text().trim();
                            }
                            break;
                        }
                    }
                    if (position == null) position = "전라북도 " + entry.getKey() + " " + title;
                    
                    data.add(new RefinedDataDTO(
                            title,
                            position,
                            Category.TOUR,
                            imgSrc,
                            introduction,
                            PATH + city.getValue()));
                }
            } catch (IOException e) {
                log.error(e.getMessage());
                return data;
            }
        }

        return data;
    }
}
