package org.jun.saemangeum.process.application.service.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jun.saemangeum.global.persistence.domain.Category;
import org.jun.saemangeum.process.application.service.base.SeleniumCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeawallTourCollector extends SeleniumCollector {

    private static final String PATH = "https://www.saemangeum.go.kr/sda/content.do?key=2010083671336";

    public SeawallTourCollector(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        webDriver.get(PATH);
        List<RefinedDataDTO> data = new ArrayList<>();

        // 탭 제목 요소
        List<WebElement> tabElements = webDriver.findElements(By.cssSelector(".tab_area .tablist li"));

        // 전체 HTML 소스 가져오기
        String html = webDriver.getPageSource();
        Document doc = Jsoup.parse(html); // 페이지 소스 갖고오는 것의 폴백 전략을 어떻게 짜야 할까?

        // tabpanel 목록
        Elements panels = doc.select(".tabpanel");

        for (int i = 0; i < tabElements.size(); i++) {
            // 탭 이름 추출
            String name = tabElements.get(i).getText().trim();
            if ("쉼터(포토존)".equals(name)) continue;

            String address = "전북특별자치도 " + name;

            // 해당 tabpanel
            Element panel = panels.get(i);

            // 이미지 src 추출
            String imgSrc = null;
            Element img = panel.selectFirst("img");
            if (img != null) {
                imgSrc = img.attr("src");
                if (imgSrc.startsWith("/")) {
                    imgSrc = "https://www.saemangeum.go.kr" + imgSrc;
                }
            }

            // 텍스트 설명 추출(태그들 빼고 줄바꿈들 정리하고 등등...)
            String rawHtml = panel.html();
            String description = rawHtml
                    .replaceAll("(?s)<img[^>]*>", "")
                    .replaceAll("(?i)<br\\s*/?>", "\n")
                    .replaceAll("<[^>]+>", "")
                    .replaceAll("&nbsp;", " ")
                    .replaceAll("&#39;", "'")
                    .trim();

            data.add(new RefinedDataDTO(name, address, Category.TOUR, imgSrc, description));
        }

        return data;
    }
}
