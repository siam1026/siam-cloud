package com.siam.package_mall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.service.PointsMallOrderLogisticsService;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_mall.entity.PointsMallOrderLogistics;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/admin/pointsMall/orderLogistics")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台订单物流跟踪信息模块相关接口", description = "AdminPointsMallOrderLogisticsController")
public class AdminPointsMallOrderLogisticsController {

    @Autowired
    private PointsMallOrderLogisticsService orderLogisticsService;

    @Operation(summary = "订单物流跟踪信息列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PointsMallOrderLogistics orderLogistics){
        BasicData basicResult = new BasicData();

        Page page = orderLogisticsService.getListByPage(orderLogistics.getPageNo(), orderLogistics.getPageSize(), orderLogistics);

        return BasicResult.success(page);
    }
}