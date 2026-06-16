package com.siam.package_merchant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.siam")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.siam", exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
@MapperScan(basePackages = {"com.siam.**.mapper"})
public class MerchantApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MerchantApplication.class, args);
    }
}