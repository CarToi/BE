package org.jun.saemangeum.process.application.collect.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.global.service.CountService;
import org.jun.saemangeum.process.application.collect.base.CrawlingCollector;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 업데이트 로직 통과
@Service
public class SeawallTourCollector extends CrawlingCollector {

    private static final String PATH = "https://www.saemangeum.go.kr/sda/content.do?key=2010083671336";

    public SeawallTourCollector(
            TitleDuplicateChecker titleDuplicateChecker,
            ContentService contentService,
            CountService countService) {
        super(titleDuplicateChecker, contentService, countService);
    }

    @Override
    public List<RefinedDataDTO> collectData() throws IOException {
        List<RefinedDataDTO> data = new ArrayList<>();

        Document doc = Jsoup.connect(PATH).timeout(5 * 1000).get();

        Elements tabs = doc.select(".tab_area .tablist li");
        Elements panels = doc.select(".tabpanel");

        if (super.isNeedToUpdate(tabs.size() - 1, CollectSource.SWTOCR)) {
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

                data.add(new RefinedDataDTO(
                        title, "전북특별자치도 " + title, Category.TOUR, imgSrc, introduction, PATH, CollectSource.SWTOCR));
            }

            return data;
        }

        return List.of();
    }
}
