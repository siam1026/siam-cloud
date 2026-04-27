package com.siam.package_order.controller.member.internal.distributed_transaction;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.Order;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.service.OrderService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member/pointsMall/order")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "订单模块相关接口", description = "OrderController")
public class DR_OrderController {

    @Resource(name = "seata_OrderServiceImpl")
    private OrderService seata_orderService;

    /**
     * [seata] 新增订单/确认下单(在发起微信支付前，前端得先请求该接口做订单校验，校验完成后接口会返回订单信息)
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/insert/bySeata")
    public BasicResult insertOrderBySeata(@RequestBody @Validated(value = {}) OrderParam param) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Order order = seata_orderService.insert(param);
        return BasicResult.success(order);
    }
}