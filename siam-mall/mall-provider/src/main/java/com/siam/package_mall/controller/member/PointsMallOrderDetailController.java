package com.siam.package_mall.controller.member;

import com.siam.package_mall.service.PointsMallOrderDetailService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/pointsMall/orderDetail")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "订单商品详情模块相关接口", description = "PointsMallOrderDetailController")
public class PointsMallOrderDetailController {
    @Autowired
    private PointsMallOrderDetailService orderDetailService;

    /*@Operation(summary = "订单商品详情列表")@PostMapping(value = "/list")
    public BasicResult list(int pageNo, int pageSize, PointsMallOrderDetail orderDetail){
        BasicData basicResult = new BasicData();

        Page page = orderDetailService.getListByPage(param.getPageNo(), param.getPageSize(), orderDetail);

        return BasicResult.success(page);
    }*/
}