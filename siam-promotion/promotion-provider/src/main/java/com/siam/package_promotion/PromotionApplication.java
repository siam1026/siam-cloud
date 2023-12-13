package com.siam.package_promotion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.siam")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.siam")
@MapperScan(basePackages = {"com.siam.**.mapper"})
public class PromotionApplication
{
    public static void main(String[] args)
    {
        // Elasticsearch与Redis有冲突，启动时elasticsearchClient无法注入，需要配置此项代码
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(PromotionApplication.class, args);
    }
}