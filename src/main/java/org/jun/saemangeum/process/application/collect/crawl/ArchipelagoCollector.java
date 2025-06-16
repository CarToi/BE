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
import org.jun.saemangeum.process.application.service.DataCountUpdateService;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 문제 없음
@Service
public class ArchipelagoCollector extends CrawlingCollector {

    private static final String URL = "https://www.sdco.or.kr";
    private static final String PATH = "/menu.es?mid=a10104000000";

    public ArchipelagoCollector(
            DataCountUpdateService dataCountUpdateService,
            TitleDuplicateChecker titleDuplicateChecker) {
        super(dataCountUpdateService, titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() throws IOException {
        List<RefinedDataDTO> data = new ArrayList<>();

        Document doc = Jsoup.connect(URL + PATH).timeout(5 * 1000).get();
        Elements items = doc.select(".list6 .item");

        if (dataCountUpdateService.isNeedToUpdate(items.size(), CollectSource.ARTOCR)) {
            for (Element item : items) {
                String title = item.select(".txt_box h5.title1").text();
                String introduction = item.select(".txt_box p.t1").text();
                String position = item.select(".txt_box p.loc").text();
                String imgSrc = item.select(".img_box img").attr("src");

                if (!imgSrc.startsWith("http")) imgSrc = URL + imgSrc;

                data.add(new RefinedDataDTO(title, position, Category.TOUR, imgSrc, introduction, URL + PATH, CollectSource.ARTOCR));
            }
            return data;
        }

        return List.of();
    }
}
