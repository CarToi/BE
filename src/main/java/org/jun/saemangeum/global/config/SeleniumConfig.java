package org.jun.saemangeum.global.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {
    @Bean
    public WebDriver webDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // GUI 없으니 헤드리스 설정
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage"); // 공유 메모리 공간 작으니

        return new ChromeDriver(options);
    }
}
