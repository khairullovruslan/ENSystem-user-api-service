package org.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EnSystemApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnSystemApiServiceApplication.class, args);
    }

}
