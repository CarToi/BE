//package org.jun.saemangeum.process.application.collect.crawl;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jun.saemangeum.global.domain.Category;
//import org.jun.saemangeum.process.application.collect.base.CrawlingCollector;
//import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
//import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//// JVM이 믿을 수 없는 SSL 인증서 체인(부안시 장난하냐)
//@Service
//public class BuanCultureCollector extends CrawlingCollector {
//
//    private static final String URL = "https://www.bacf.or.kr";
//    private static final List<String> PAGES = List.of(
//            "/base/contents/view?contentsNo=11&menuLevel=2&menuNo=14",
//            "/base/contents/view?contentsNo=12&menuLevel=2&menuNo=15",
//            "/base/contents/view?contentsNo=13&menuLevel=2&menuNo=16",
//            "/base/contents/view?contentsNo=90&menuLevel=2&menuNo=58");
//
//    public BuanCultureCollector(TitleDuplicateChecker titleDuplicateChecker) {
//        super(titleDuplicateChecker);
//    }
//
//    @Override
//    public List<RefinedDataDTO> collectData() throws IOException {
//        List<RefinedDataDTO> data = new ArrayList<>();
//
//        for (String page : PAGES) {
//            Document doc = Jsoup.connect(URL + page).timeout(5 * 1000).get();
//            Element element = doc.selectFirst(".facility-contents-top__text span");
//            if (element == null) continue;
//
//            String title = element.text();
//
//            Element introElement = doc.select(".facility-contents-bottom__info p").first();
//            String introduction = introElement != null ? introElement.text() : null;
//
//            String position = doc.select(".facility-contents-bottom__status li")
//                    .stream()
//                    .filter(el -> el.text().contains("소재지"))
//                    .findFirst()
//                    .map(Element::text)
//                    .map(text -> text.replace("소재지 : ", "").trim())
//                    .orElse(null);
//
//            Element imgElement = doc.selectFirst(".facility-contents-bottom__img .item img");
//            String imgSrc = imgElement != null ? URL + imgElement.attr("src") : null;
//
//            System.out.println("명칭: " + title);
//            System.out.println("이미지: " + imgSrc);
//            System.out.println("설명: " + introduction);
//            System.out.println("위치: " + position);
//
//            data.add(new RefinedDataDTO(title, position, Category.CULTURE, imgSrc, introduction, URL + page));
//        }
//
//        return data;
//    }
//}
