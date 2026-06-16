package com.siam.package_user.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.DeliveryAddress;
import com.siam.package_user.service.DeliveryAddressService;
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
@RequestMapping(value = "/rest/admin/deliveryAddress")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "后台收货地址模块相关接口", description = "AdminDeliveryAddressController")
public class AdminDeliveryAddressController {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Operation(summary = "收货地址列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) DeliveryAddress param) {
        BasicData basicResult = new BasicData();

        Page page = deliveryAddressService.getListByPage(param.getPageNo(), param.getPageSize(), param);

        return BasicResult.success(page);
    }

    @Operation(summary = "新增收货地址")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) DeliveryAddress param) {
        BasicResult basicResult = new BasicResult();

        deliveryAddressService.insertSelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "修改收货地址")
    @PostMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) DeliveryAddress param) {
        BasicResult basicResult = new BasicResult();

        deliveryAddressService.updateByPrimaryKeySelective(param);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }

    @Operation(summary = "删除收货地址")
    @PostMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) DeliveryAddress param) {
        BasicResult basicResult = new BasicResult();

        deliveryAddressService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
}