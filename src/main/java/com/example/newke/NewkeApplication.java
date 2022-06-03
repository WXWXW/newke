package com.example.newke;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.newke.dao")
public class NewkeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewkeApplication.class, args);
    }

}
