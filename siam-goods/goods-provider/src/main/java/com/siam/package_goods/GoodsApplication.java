package com.siam.package_goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableFeignClients("com.siam")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.siam", exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
@MapperScan(basePackages = {"com.siam.**.mapper"})
public class GoodsApplication
{
    public static void main(String[] args)
    {
        // Elasticsearch与Redis有冲突，启动时elasticsearchClient无法注入，需要配置此项代码
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(GoodsApplication.class, args);
    }
}