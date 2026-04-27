package com.siam.package_mall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_mall.service.PointsMallOrderDetailService;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_mall.entity.PointsMallOrderDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/admin/pointsMall/orderDetail")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台订单商品详情模块相关接口", description = "AdminPointsMallOrderDetailController")
public class AdminPointsMallOrderDetailController {
    @Autowired
    private PointsMallOrderDetailService orderDetailService;

    @Operation(summary = "订单商品详情列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) PointsMallOrderDetail orderDetail){
        BasicData basicResult = new BasicData();

        Page page = orderDetailService.getListByPage(orderDetail.getPageNo(), orderDetail.getPageSize(), orderDetail);

        return BasicResult.success(page);
    }


    @Operation(summary = "新增订单商品详情")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) PointsMallOrderDetail orderDetail){
        BasicResult basicResult = new BasicResult();

        orderDetailService.insertSelective(orderDetail);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "修改订单商品详情")@PostMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) PointsMallOrderDetail orderDetail){
        BasicResult basicResult = new BasicResult();

        orderDetailService.updateByPrimaryKeySelective(orderDetail);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    @Operation(summary = "删除订单商品详情(含批量操作)")@PostMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) PointsMallOrderDetail param){
        BasicResult basicResult = new BasicResult();

        for(String id : param.getIds()){
            orderDetailService.deleteByPrimaryKey(Integer.valueOf(id));
        }

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }


}