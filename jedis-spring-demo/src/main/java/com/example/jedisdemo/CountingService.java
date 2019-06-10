package com.example.jedisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableCaching(proxyTargetClass = true)
@CacheConfig(cacheNames = "count")
public class CountingService {

    @CacheEvict
    public void clearCount() {
    }

    @Cacheable
    public int getNumber() {
        log.error("Reading from cache.......");
        return 1000;
    }
}
