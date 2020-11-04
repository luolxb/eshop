package com.soubao;

import com.baomidou.jobs.starter.EnableJobs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableTransactionManagement
@EnableJobs
public class MallApplication {
	public static void main(String[] args) {
		//在main方法里插入下面的代码以解决es的netty冲突问题
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(MallApplication.class, args);
	}
}
