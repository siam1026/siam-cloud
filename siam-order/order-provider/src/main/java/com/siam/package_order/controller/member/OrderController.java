package com.siam.package_order.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_order.entity.Order;
import com.siam.package_order.model.param.OrderParam;
import com.siam.package_order.model.vo.OrderVo;
import com.siam.package_order.model.vo.OrderVo2;
import com.siam.package_order.service.*;
import com.siam.package_order.service.OrderService;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_user.util.TokenUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/rest/member/order")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "订单模块相关接口", description = "OrderController")
public class OrderController {

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "rewardServiceImpl")
    private RewardService rewardService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MemberSessionManager memberSessionManager;

    /**
     * 订单列表
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/list")
    public BasicResult listOrder(@RequestBody @Validated(value = {}) OrderParam param){
        Page<Map<String, Object>> page = orderService.getListByPageWithDesc(param);
        return BasicResult.success(page);
    }

    /**
     * 查看订单详情
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestBody @Validated(value = {}) OrderParam param){
        OrderVo orderVo = orderService.selectById(param);
        return BasicResult.success(orderVo);
    }

    /**
     * 新增订单/确认下单(在发起微信支付前，前端得先请求该接口做订单校验，校验完成后接口会返回订单信息)
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/insert")
    public BasicResult insertOrder(@RequestBody @Validated(value = {}) OrderParam param) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        // Redis 分布式锁 - 防重复下单
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());
        String lockKey = "order:create:lock:" + loginMember.getId();
        Boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(isLocked)) {
            throw new StoneCustomerException("正在处理中，请勿重复点击");
        }
        try {
            Order order = orderService.insert(param);
            return BasicResult.success(order);
        } finally {
            // 订单创建是长流程（含分布式事务），此处不主动删锁，依赖 5 秒过期防止并发
        }
    }

    /**
     * 取消订单(五分钟内未付款，自动取消)
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/cancelOrder")
    public BasicResult cancelOrderByUnPaid(@RequestBody @Validated(value = {}) OrderParam param){
        orderService.cancelOrderByUnPaid(param);
        return BasicResult.success();
    }

    /**
     * 取消订单(已支付)/极速退款 - 一分钟内取消订单
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/cancelOrderNoReason")
    public BasicResult cancelOrderNoReason(@RequestBody @Validated(value = {}) OrderParam param) throws IOException {
        orderService.cancelOrderNoReason(param);
        return BasicResult.success();
    }

    /**
     * 申请退款
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/applyRefund")
    public BasicResult applyRefund(@RequestBody @Validated(value = {}) OrderParam param) throws IOException {
        orderService.applyRefund(param);
        return BasicResult.success();
    }

    /**
     * 【废弃，外卖系统没用到，积分商城才会用到】
     * @param param
     * @return
     */
    @Operation(summary = "修改订单状态为已确认收货")
    @PostMapping(value = "/confirmReceipt")
    public BasicResult confirmReceipt(@RequestBody @Validated(value = {}) OrderParam param){
        orderService.confirmReceipt(param);
        return BasicResult.success();
    }

    @Operation(summary = "删除订单")

    @PostMapping(value = "/delete")
    public BasicResult deleteOrder(@RequestBody @Validated(value = {}) OrderParam param){
        orderService.delete(param);
        return BasicResult.success();
    }

    /**
     * 查看订单退款进度
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/selectRefundProcess")
    public BasicResult selectRefundProcess(@RequestBody @Validated(value = {}) OrderParam param){
        OrderVo2 orderVo2 = orderService.selectRefundProcess(param);
        return BasicResult.success(orderVo2);
    }

    /**
     * 查询标签页的记录数量
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/selectTabNumber")
    public BasicResult selectTabNumber(@RequestBody @Validated(value = {}) OrderParam param){
        Map map = orderService.selectTabNumber(param);
        return BasicResult.success(map);
    }

    /**
     * 查看佣金奖励信息
     *
     * @return
     * @author 暹罗
     */
    @PostMapping(value = "/selectCommissionReward")
    public BasicResult selectCommissionReward(@RequestBody @Validated(value = {}) OrderParam param){
        String text = rewardService.selectCommissionReward(param);
        return BasicResult.success(text);
    }
}