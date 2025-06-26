package org.jun.saemangeum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SaemangeumApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaemangeumApplication.class, args);
    }

}
