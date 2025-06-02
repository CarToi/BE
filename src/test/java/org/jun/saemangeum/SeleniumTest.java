package org.jun.saemangeum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTest {

    WebDriver driver;
    String SDIA_LINK = "https://www.saemangeum.go.kr/sda/content.do?key=2010083672101";

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // GUI 없으니 헤드리스 설정
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 공간 작으니

        driver = new ChromeDriver(options);
    }

    @Test
    @DisplayName("공식 문서 기반 간단한 동작 테스트")
    void test1() {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");

        String title = driver.getTitle();
        assertEquals("Web form", title);

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        String value = message.getText();
        assertEquals("Received!", value);
    }

    @Test
    @DisplayName("새만금 개발청 링크 활용한 테스트")
    void test2() {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(SDIA_LINK);

        List<WebElement> items = driver.findElements(By.cssSelector("li.flo_right, li.flo_left"));
        assertEquals(items.size(), 10);

        for (WebElement item : items) {
            String title = item.findElement(By.cssSelector("h5")).getText();
            List<WebElement> dts = item.findElements(By.cssSelector("dt"));
            List<WebElement> dds = item.findElements(By.cssSelector("dd"));

            StringBuilder info = new StringBuilder();

            for (int i = 0; i < dts.size(); i++) {
                String label = dts.get(i).getText();
                String value = dds.get(i).getText();
                info.append(label).append(": ").append(value).append("\n");
            }

            String imageUrl = item.findElement(
                    By.cssSelector(".thumb img.disp_pc")).getDomProperty("src");

            String homepage = "";
            List<WebElement> homeLinks = item.findElements(By.cssSelector(".btnwrap a"));
            if (!homeLinks.isEmpty()) {
                homepage = homeLinks.getFirst().getDomProperty("href");
            }

            System.out.println("==================================");
            System.out.println("제목: " + title);
            System.out.println("이미지: " + imageUrl);
            System.out.println(info);

            assert homepage != null;
            if (!homepage.isEmpty()) {
                System.out.println("홈페이지: " + homepage);
            }
        }
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }
}
