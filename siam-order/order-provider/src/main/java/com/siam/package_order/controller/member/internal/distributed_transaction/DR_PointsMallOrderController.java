package com.siam.package_order.controller.member.internal.distributed_transaction;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_order.entity.internal.PointsMallOrder;
import com.siam.package_order.model.param.internal.PointsMallOrderParam;
import com.siam.package_order.service.internal.PointsMallOrderService;
import io.swagger.annotations.Api;
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

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member/pointsMall/order")
@Transactional(rollbackFor = Exception.class)
@Api(tags = "订单模块相关接口", description = "PointsMallOrderController")
public class DR_PointsMallOrderController {

    @Resource(name = "seata_PointsMallOrderServiceImpl")
    private PointsMallOrderService seata_orderService;

    /**
     * [seata] 新增订单/确认下单(在发起微信支付前，前端得先请求该接口做订单校验，校验完成后接口会返回订单信息)
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/insert/bySeata")
    public BasicResult insertPointsMallOrderBySeata(@RequestBody @Validated(value = {}) PointsMallOrderParam param) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        PointsMallOrder pointsMallOrder = seata_orderService.insert(param);
        return BasicResult.success(pointsMallOrder);
    }
}