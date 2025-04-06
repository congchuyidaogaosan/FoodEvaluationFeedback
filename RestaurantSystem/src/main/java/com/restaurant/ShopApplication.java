package com.restaurant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.restaurant.mapper")
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.restaurant.ShopApplication.class, args);
    }

}
