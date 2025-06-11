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

@Slf4j
@Service
public class ArchipelagoCollector extends CrawlingCollector {

    private static final String URL = "https://www.sdco.or.kr";
    private static final String PATH = "/menu.es?mid=a10104000000";

    @Override
    public List<RefinedDataDTO> collectData() {
        List<RefinedDataDTO> data = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL + PATH).timeout(5 * 1000).get();
            Elements items = doc.select(".list6 .item");

            for (Element item : items) {
                String title = item.select(".txt_box h5.title1").text();
                String introduction = item.select(".txt_box p.t1").text();
                String position = item.select(".txt_box p.loc").text();
                String imgSrc = item.select(".img_box img").attr("src");

                if (!imgSrc.startsWith("http")) imgSrc = URL + imgSrc;

                log.info(introduction);

                data.add(new RefinedDataDTO(title, position, Category.TOUR, imgSrc, introduction, URL + PATH));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return data;
        }

        return data;
    }
}
