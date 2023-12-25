package com.siam.package_mall;

import com.siam.package_common.util.AliyunExpressUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
public class MallApplicationTest {

    @Autowired
    private AliyunExpressUtils aliyunExpressUtils;

    /**
     * 测试发送微信服务通知、微信公众号消息
     */
    @Test
    public void testSendWxMessage() throws IOException {

    }
}