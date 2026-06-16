package com.siam.package_mall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_common.model.valid_group.ValidGroupOfAudit;
import com.siam.package_common.model.valid_group.ValidGroupOfId;
import com.siam.package_mall.model.param.PointsMallOrderParam;
import com.siam.package_mall.model.result.PointsMallOrderResult;
import com.siam.package_mall.service.PointsMallOrderService;
import com.siam.package_mall.entity.PointsMallOrder;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping(value = "/rest/admin/pointsMall/order")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台订单模块相关接口", description = "AdminPointsMallOrderController")
public class AdminPointsMallOrderController {

    @Resource(name = "pointsMallOrderServiceImpl")
    private PointsMallOrderService orderService;

    @Operation(summary = "订单列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Page page = orderService.getListByPageWithAsc(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "订单列表(附带订单商品详情)")
    @PostMapping(value = "/listWithDetail")
    public BasicResult listWithDetail(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Page<PointsMallOrderResult> page = orderService.getListByPageWithAsc(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "打印小票")
    @PostMapping(value = "/printRceceipts")
    public BasicResult printRceceipts(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        for(String id : param.getIds()){
            orderService.printRceceipts(Long.valueOf(id));
        }
        return BasicResult.success();
    }

    @Operation(summary = "查询单个订单信息")
    @PostMapping(value = "/selectById")
    public BasicResult selectById(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        PointsMallOrder dbPointsMallOrder = orderService.selectByPrimaryKey(param.getId());
        if(dbPointsMallOrder == null){
            throw new StoneCustomerException("该订单不存在");
        }
        return BasicResult.success(dbPointsMallOrder);
    }

    @Operation(summary = "批量修改订单的是否已打印状态为已打印")
    @PostMapping(value = "/batchUpdateIsPrintedTrue")
    public BasicResult batchUpdateIsPrintedTrue(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        orderService.batchUpdateIsPrintedTrue(param);
        return BasicResult.success();
    }

    @Operation(summary = "今日订单列表")
    @PostMapping(value = "/todayPointsMallOrderList")
    public BasicResult todayPointsMallOrderList(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Page page = orderService.getListByTodayOrderWithAsc(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "查询所有订单标签页的待处理数量")
    @PostMapping(value = "/selectAllTabWaitHandleNum")
    public BasicResult selectAllTabWaitHandleNum(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Map map = orderService.selectAllTabWaitHandleNum(param);
        return BasicResult.success(map);
    }

    @Operation(summary = "查询所有已付款未打印的订单")
    @PostMapping(value = "/waitPrintPointsMallOrderList")
    public BasicResult waitPrintPointsMallOrderList(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        List<PointsMallOrder> pointsMallOrders = orderService.waitPrintPointsMallOrderList(param);
        return BasicResult.success(pointsMallOrders);
    }

    @Operation(summary = "订单统计(支付成功订单数量、取消订单数量、退款订单数量，按自取或者外卖分开)")
    @PostMapping(value = "/countPointsMallOrder")
    public BasicResult countPointsMallOrder(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Map count = orderService.countOrder(param);
        return BasicResult.success(count);
    }

    @Operation(summary = "售后处理订单列表")
    @PostMapping(value = "/afterSalesList")
    public BasicResult afterSalesList(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Page<Map<String, Object>> page = orderService.getAfterSalesListByPageWithAsc(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "售后处理订单列表")
    @PostMapping(value = "/afterSalesListWithDetail")
    public BasicResult afterSalesListWithDetail(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        Page<Map<String, Object>> page = orderService.getAfterSalesListByPageWithAsc(param);
        return BasicResult.success(page);
    }

    @PostMapping(value = "/auditAfterSalesOrder")
    public BasicResult auditAfterSalesOrder(@RequestBody @Validated(value = {ValidGroupOfId.class, ValidGroupOfAudit.class}) PointsMallOrderParam orderParam){
        orderService.auditAfterSalesOrder(orderParam);
        return BasicResult.success();
    }

    @PostMapping(value = "/statistic")
    public BasicResult statisticPointsMallOrder(@RequestBody @Validated(value = {}) PointsMallOrderParam param) throws ParseException {
        Map map = orderService.statistic(param);
        return BasicResult.success(map);
    }

    @PostMapping(value = "/deliver")
    public BasicResult deliver(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        orderService.deliver(param);
        return BasicResult.success();
    }

    @PostMapping(value = "/updateLogisticsNo")
    public BasicResult updateLogisticsNo(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        orderService.updateLogisticsNo(param);
        return BasicResult.success();
    }

    @PostMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) PointsMallOrderParam param){
        orderService.updateByAdmin(param);
        return BasicResult.success();
    }
}
