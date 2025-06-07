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
public class SeawallTourCollector extends CrawlingCollector {

    private static final String PATH = "https://www.saemangeum.go.kr/sda/content.do?key=2010083671336";

    @Override
    public List<RefinedDataDTO> collectData() {
        List<RefinedDataDTO> data = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(PATH).timeout(5 * 1000).get();

            Elements tabs = doc.select(".tab_area .tablist li");
            Elements panels = doc.select(".tabpanel");

            for (int i = 0; i < tabs.size(); i++) {
                String name = tabs.get(i).text().trim();
                if ("쉼터(포토존)".equals(name)) continue;

                String address = "전북특별자치도 " + name;

                Element panel = panels.get(i);

                String imgSrc = null;
                Element img = panel.selectFirst("img");
                if (img != null) {
                    imgSrc = img.attr("src");
                    if (imgSrc.startsWith("/")) {
                        imgSrc = "https://www.saemangeum.go.kr" + imgSrc;
                    }
                }

                String rawHtml = panel.html();
                String description = rawHtml
                        .replaceAll("(?s)<img[^>]*>", "")
                        .replaceAll("(?i)<br\\s*/?>", "\n")
                        .replaceAll("<[^>]+>", "")
                        .replace("&nbsp;", " ")
                        .replace("&#39;", "'")
                        .trim();

                System.out.println("이름: " + name);
                System.out.println("주소: " + address);
                System.out.println("이미지: " + imgSrc);
                System.out.println("설명: " + description);
                System.out.println("--------------------------------------------------");

                data.add(new RefinedDataDTO(name, address, Category.TOUR, imgSrc, description));
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return data;
    }
}
