package org.jun.saemangeum.process.application.service.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jun.saemangeum.global.persistence.domain.Category;
import org.jun.saemangeum.process.application.service.base.CrawlingCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GunsanTourCollector extends CrawlingCollector {

    private static final String PATH = "https://www.saemangeum.go.kr/sda/content.do?key=2009163854184";

    @Override
    public List<RefinedDataDTO> collectData() {
        List<RefinedDataDTO> data = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(PATH).timeout(5 * 1000).get();
            Elements items = doc.select("ul.li_spot.gunsan > li");

            for (Element item : items) {
                Element element = item.selectFirst("div.info_spot > h5");
                if (element == null) continue;

                String title = element.text().trim();

                // 설명 <p> 여러 개 있는 경우 모두 합치기
                Elements descElements = item.select("div.info_spot > p");
                StringBuilder descBuilder = new StringBuilder();
                for (Element p : descElements) {
                    descBuilder.append(p.text().trim()).append("\n");
                }
                String introduction = descBuilder.toString().trim();

                // 이미지 URL
                Element imgEl = item.selectFirst("div.thumb img");
                String imgSrc = imgEl != null ? "https://www.saemangeum.go.kr" + imgEl.attr("src") : null;

                System.out.println("이름: " + title);
                System.out.println("이미지: " + imgSrc);
                System.out.println("설명: " + introduction);
                System.out.println("--------------------------------------------------");

                data.add(new RefinedDataDTO(title, "전북특별자치도 " + title, Category.TOUR, imgSrc, introduction));
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return data;
    }
}
