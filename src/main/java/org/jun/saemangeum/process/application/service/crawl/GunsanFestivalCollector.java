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
public class GunsanFestivalCollector extends CrawlingCollector {

    private static final String URL = "https://www.gunsan.go.kr";
    private static final List<String> PAGES = List.of(
            "/3304137?", "/1504859?", "/1449917?", "/1448120?", "/306508?",
            "/306507?", "/306505?", "/306504?", "/306503?", "/306502?");

    @Override
    public List<RefinedDataDTO> collectData() {
        List<RefinedDataDTO> data = new ArrayList<>();

        for (String page : PAGES) {
            try {
                Document doc = Jsoup.connect(URL + "/tour/m2101/view" + page).timeout(5 * 1000).get();

                Element element = doc.selectFirst(".fieldBasic li.i-1 p");
                if (element == null) continue;
                String title = element.text();

                Element imgEl = doc.selectFirst(".viewImg img");
                String imgSrc = imgEl != null ? "https://www.gunsan.go.kr" + imgEl.attr("src") : null;

                Element positionEl = doc.selectFirst(".fieldBasic li.i-3 p");
                String position = positionEl != null ? positionEl.text() : "전북 군산시 " + title;

                Elements introductionEl = doc.select(".conText");
                String introduction = introductionEl.text().trim();

                data.add(new RefinedDataDTO(
                        title, position, Category.FESTIVAL, imgSrc, introduction));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        return data;
    }
}
