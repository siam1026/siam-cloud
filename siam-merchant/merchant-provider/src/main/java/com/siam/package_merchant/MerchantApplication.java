package com.siam.package_merchant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.siam")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.siam")
@MapperScan(basePackages = {"com.siam.**.mapper"})
public class MerchantApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MerchantApplication.class, args);
    }
}