package com.example.springdataredis;

import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
@EnableCaching
public class SpringDataRedisApplication implements ApplicationRunner {

    @Autowired
    private CountingService countingService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, HUser> redisUserTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        springDataRedisDemo();
//        redisTemplateSimpleDemo();
        redisAddEntity();
    }


    @Bean
    public RedisTemplate<String, HUser> redisUserTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, HUser> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisObjectTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    @Bean
    public LettuceClientConfigurationBuilderCustomizer customizer() {
        //因为配置了spring.redis.lettuce.pool,lettuce配置的bean不可缺少。可以采用如下几种方式
        //LettuceClientConfiguration
        //LettucePoolingClientConfiguration
        //LettuceClientConfigurationBuilderCustomizer

        // lettuce支持读写分离
        // 只读主、只读从
        // 优先读主、优先读从
        //这里设置从主节点读取
        return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
    }


    private void springDataRedisDemo() {
        log.info("Count: {}", countingService.getNumber());
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                countingService.clearCount();
            }
            log.info("time:{} Count: {}", i, countingService.getNumber());
        }
    }

    private void redisTemplateSimpleDemo() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("123", "hello");
        properties.put("456", "world");
        properties.put("abc", 456);
        //存
        redisTemplate.opsForHash().putAll("hash", properties);
        //设置过期时间
        //redisTemplate.expireAt("hash",date)
        redisTemplate.expire("hash",20, TimeUnit.SECONDS);
        //取
        Map<Object, Object> ans = redisTemplate.opsForHash().entries("hash");
        log.info(ans.toString());
    }


    private void redisAddEntity() {
        HUser user = new HUser();
        user.setAge(11);
        user.setId("abcdefg");
        user.setNickName("Ken");
        redisUserTemplate.opsForValue().set(user.getId(), user);
        //设置过期时间
        redisUserTemplate.expire(user.getId(),20,TimeUnit.SECONDS);
        //判断key是否存在
        if (redisUserTemplate.hasKey(user.getId())){
            HUser u2 = redisUserTemplate.opsForValue().get("user:ken");
            log.info(u2.toString());
        }
    }

}













