package org.jun.saemangeum.process.application.collect.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.process.application.collect.base.CrawlingCollector;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GunsanFestivalCollector extends CrawlingCollector {

    private static final String URL = "https://www.gunsan.go.kr";
    private static final String MENU = "/tour/m2081";
    private static final List<String> PAGES = List.of(
            "/3304137?", "/1504859?", "/1449917?", "/1448120?", "/306508?",
            "/306507?", "/306505?", "/306504?", "/306503?", "/306502?");

    public GunsanFestivalCollector(TitleDuplicateChecker titleDuplicateChecker) {
        super(titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() throws IOException {
        List<RefinedDataDTO> data = new ArrayList<>();

        for (String page : PAGES) {
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

            if (introduction.length() > 650) {

                List<String> contentBlocks = new ArrayList<>();

                for (Element child : Objects.requireNonNull(introductionEl.first()).children()) {
                    String text = child.text().trim();

                    // 너무 짧은 건 무시 (ex: 제목, 공백 등)
                    if (text.length() > 30 && text.length() < 200) {
                        contentBlocks.add(text);
                    }
                }

                if (!contentBlocks.isEmpty()) {
                    if (contentBlocks.size() >= 2) {
                        introduction =
                                contentBlocks.get(contentBlocks.size() - 2) + contentBlocks.getLast();
                    } else {
                        introduction = contentBlocks.getFirst();
                    }
                }
            }

            log.info(introduction);

            data.add(new RefinedDataDTO(
                    title, position, Category.FESTIVAL, imgSrc, introduction, URL + MENU));
        }

        return data;
    }
}
