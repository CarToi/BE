//package org.jun.saemangeum.connect;
//
//import org.junit.jupiter.api.*;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class SeleniumTest {
//
//    @Autowired
//    private WebDriver webDriver;
//
//    @Test
//    @DisplayName("새만금 개발청 링크 활용한 테스트")
//    void test() {
//        webDriver.get("https://www.saemangeum.go.kr/sda/content.do?key=2010083672101");
//
//        List<WebElement> items = webDriver.findElements(By.cssSelector("li.flo_left, li.flo_right"));
//        assertEquals(items.size(), 10);
//
//        for (WebElement item : items) {
//            String title = item.findElement(By.cssSelector("h5")).getText();
//            List<WebElement> dts = item.findElements(By.cssSelector("dt"));
//            List<WebElement> dds = item.findElements(By.cssSelector("dd"));
//
//            Assertions.assertNotNull(title);
//            Assertions.assertEquals(dts.size(), 3);
//            Assertions.assertEquals(dds.size(), 3);
//
//            String imageUrl = item.findElement(
//                    By.cssSelector(".thumb img.disp_pc")).getDomProperty("src");
//
//            String homepage = "";
//            List<WebElement> homeLinks = item.findElements(By.cssSelector(".btnwrap a"));
//            if (!homeLinks.isEmpty()) {
//                homepage = homeLinks.getFirst().getDomProperty("href");
//            }
//
//            Assertions.assertNotNull(imageUrl);
//            Assertions.assertNotNull(homepage);
//        }
//    }
//
//    @AfterEach
//    void teardown() {
//        webDriver.quit();
//    }
//}
