package org.jun.saemangeum.process.infrastructure.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SeleniumConfig {
    @Bean
    public WebDriver webDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // GUI 없으니 헤드리스 설정
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 공간 작으니

        WebDriver webDriver = new ChromeDriver(options);
        // 시간 초과되면 NoSuchElementException throw
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        return webDriver;
    }
}
