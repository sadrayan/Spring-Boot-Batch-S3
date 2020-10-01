package com.nellietech.batch;

import com.nellietech.batch.service.S3Service;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
//        S3Service service = applicationContext.getBean(S3Service.class);
//        service.getFile("data.csv");
    }

}
