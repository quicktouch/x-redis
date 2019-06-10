package com.example.jedisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class JedisDemoApplication implements ApplicationRunner {

    @Autowired
    private CountingService countingService;

    public static void main(String[] args) {
        SpringApplication.run(JedisDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Count: {}", countingService.getNumber());
        for (int i = 0; i < 10; i++) {
            if (i == 5){
                countingService.clearCount();
            }
            log.info("time:{} Count: {}", i, countingService.getNumber());
        }
    }
}
