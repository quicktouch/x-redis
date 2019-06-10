package com.example.springdataredis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@CacheConfig(cacheNames = "count")
public class CountingService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;  //不一定是string，也可以是实体类

    @CacheEvict //清除缓存
    public void clearCount() {
    }

    @Cacheable  //开启缓存
    public int getNumber() {
        log.error("Reading from cache.......");
        return 1000;
    }

    // Spring 3.X
    //@Cacheable(value="artisan")

    // Spring 4.0新增了value的别名cacheNames，更贴切，推荐使用
    //@Cacheable(cacheNames="artisan")

    public String getRedisCacheStringValue(){
         String key = "123456789";
         String hashKey = "hasKey";
         HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
         if (redisTemplate.hasKey(key) && hashOperations.hasKey(key,hashKey)){
         }
         return "";
    }
}















