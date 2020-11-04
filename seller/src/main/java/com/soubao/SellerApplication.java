package com.soubao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.soubao.service")
public class SellerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SellerApplication.class, args);
	}
}
