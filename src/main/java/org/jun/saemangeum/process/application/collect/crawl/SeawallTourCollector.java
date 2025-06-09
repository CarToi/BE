package org.jun.saemangeum.process.application.collect.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jun.saemangeum.global.persistence.domain.Category;
import org.jun.saemangeum.process.application.collect.base.CrawlingCollector;
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
                String title = tabs.get(i).text().trim();
                if ("쉼터(포토존)".equals(title)) continue;

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
                String introduction = rawHtml
                        .replaceAll("(?s)<img[^>]*>", "")
                        .replaceAll("(?i)<br\\s*/?>", "\n")
                        .replaceAll("<[^>]+>", "")
                        .replace("&nbsp;", " ")
                        .replace("&#39;", "'")
                        .trim();

                data.add(new RefinedDataDTO(title, "전북특별자치도 " + title, Category.TOUR, imgSrc, introduction));
            }

        } catch (IOException e) {
            // 어떤 폴백 전략을 세울 수 있을까?
            log.error(e.getMessage());
            return data;
        }

        return data;
    }
}
