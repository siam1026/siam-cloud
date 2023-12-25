package com.siam.package_order.internal;

import com.siam.package_common.util.JsonUtils;
import com.siam.package_order.OrderApplication;
import com.siam.package_order.entity.Order;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 分布式事务相关
 *
 * @return
 * @author 暹罗
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties
public class DistributedTransactionTest {

    @Resource(name = "localMsg_OrderServiceImpl")
    private OrderService localMsg_orderService;

    @Resource(name = "seata_OrderServiceImpl")
    private OrderService seata_orderService;

    @Resource(name = "rocketMQ_OrderServiceImpl")
    private OrderService rocketMQ_orderService;

    /**
     * 本地消息表
     *
     * @return
     * @author 暹罗
     */
    @Test
    public void localMsgTest() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //1、订单服务-新增订单记录
        //2、商品服务-购物车数量扣减
        //3、用户服务-用户状态变更

        //本地消息表是在调用其他微服务之前插入，还是在之后插入的问题 类似于 悲观锁、乐观锁的概念；应该只能在之前插入，因为中间过程可能会中断的；
        //TODO(MARK) - 本地消息表有很多问题，比如服务一多就容易出现套娃现象、回滚的问题，所以我觉得实现起来没啥太大意义，暂时先放弃实现了，理论搞懂就行；

        String json = "";
        OrderParam param = (OrderParam) JsonUtils.toObject(json, OrderParam.class);
        Order order = localMsg_orderService.insert(param);
    }

    /**
     * rocketMQ事务消息
     *
     * @return
     * @author 暹罗
     */
    @Test
    public void rocketMQTest() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        //TODO(MARK) - 大致看了下三四个示例，流程差不多，也没有回滚操作，说是回滚的成本太大了，只能人工干预；
        String json = "{\"deliveryAddressId\":592,\"deliveryFee\":0,\"shoppingCartIdList\":[124],\"orderDetailList\":[{\"goodsId\":7,\"goodsName\":\"舒缓新肌焕颜乳\",\"specList\":\"{}\",\"number\":1}],\"actualPrice\":138,\"remark\":\"\",\"shoppingWay\":1,\"orderToken\":\"df8f0549b3834b54809e89801cd5630b\"}";
        OrderParam param = (OrderParam) JsonUtils.toObject(json, OrderParam.class);
        Order order = rocketMQ_orderService.insert(param);
        System.out.println(1);
    }

    /**
     * seata-AT模式
     *
     * @return
     * @author 暹罗
     */
    @Test
    public void seataTest() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String json = "{\"deliveryAddressId\":592,\"deliveryFee\":0,\"shoppingCartIdList\":[124],\"orderDetailList\":[{\"goodsId\":7,\"goodsName\":\"舒缓新肌焕颜乳\",\"specList\":\"{}\",\"number\":1}],\"actualPrice\":138,\"remark\":\"\",\"shoppingWay\":1,\"orderToken\":\"df8f0549b3834b54809e89801cd5630b\"}";
        OrderParam param = (OrderParam) JsonUtils.toObject(json, OrderParam.class);
        Order order = seata_orderService.insert(param);
        System.out.println(1);
    }
}