package com.wit.travel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Spring Boot启动类
 * 个性化旅游推荐平台主程序入口
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.wit.travel.mapper")
public class TravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
        System.out.println("========================================");
        System.out.println("个性化旅游推荐平台启动成功！");
        System.out.println("访问地址: http://localhost:8080/api");
        System.out.println("========================================");
    }
}
