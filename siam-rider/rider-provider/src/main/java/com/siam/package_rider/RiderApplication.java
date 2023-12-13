package com.siam.package_rider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.siam")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.siam")
@MapperScan(basePackages = {"com.siam.**.mapper"})
public class RiderApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RiderApplication.class, args);
    }
}