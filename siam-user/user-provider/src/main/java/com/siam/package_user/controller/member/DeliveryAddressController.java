package com.siam.package_user.controller.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.constant.Quantity;
import com.siam.package_user.entity.DeliveryAddress;
import com.siam.package_user.service.DeliveryAddressService;
import com.siam.package_user.auth.cache.MemberSessionManager;
import com.siam.package_user.entity.Member;
import com.siam.package_user.util.TokenUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/rest/member/deliveryAddress")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "收货地址模块相关接口", description = "DeliveryAddressController")
public class DeliveryAddressController {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private MemberSessionManager memberSessionManager;

    @Operation(summary = "收货地址列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) DeliveryAddress deliveryAddress, HttpServletRequest request){
        BasicData basicResult = new BasicData();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        deliveryAddress.setMemberId(loginMember.getId());
        Page page = deliveryAddressService.getListByPage(deliveryAddress.getPageNo(), deliveryAddress.getPageSize(), deliveryAddress);

        return BasicResult.success(page);
    }

    @Operation(summary = "新增收货地址")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) DeliveryAddress deliveryAddress, HttpServletRequest request){
        BasicData basicResult = new BasicData();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        deliveryAddress.setMemberId(loginMember.getId());
        deliveryAddress.setCreateTime(new Date());
        deliveryAddress.setUpdateTime(new Date());
        deliveryAddressService.insertSelective(deliveryAddress);

        if(deliveryAddress.getIsDefault() == Quantity.INT_1){
            deliveryAddressService.updateIsDefaultExclusion(deliveryAddress.getId(), deliveryAddress.getMemberId());
        }

        basicResult.setData(deliveryAddress.getId());
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("新增成功");
        return basicResult;
    }

    @Operation(summary = "修改收货地址")
    @PostMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) DeliveryAddress deliveryAddress, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        DeliveryAddress dbDeliveryAddress = deliveryAddressService.selectByPrimaryKey(deliveryAddress.getId());
        if(dbDeliveryAddress == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该收货地址不存在");
            return basicResult;
        }

        if(!dbDeliveryAddress.getMemberId().equals(loginMember.getId())){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该收货地址不是你的，不允许修改");
            return basicResult;
        }

        deliveryAddress.setUpdateTime(new Date());
        deliveryAddressService.updateByPrimaryKeySelective(deliveryAddress);

        if(deliveryAddress.getIsDefault() == Quantity.INT_1){
            deliveryAddressService.updateIsDefaultExclusion(dbDeliveryAddress.getId(), dbDeliveryAddress.getMemberId());
        }

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }


    @Operation(summary = "删除收货地址")
    @PostMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) DeliveryAddress param){
        BasicResult basicResult = new BasicResult();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        DeliveryAddress dbDeliveryAddress = deliveryAddressService.selectByPrimaryKey(param.getId());
        if(dbDeliveryAddress == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该收货地址不存在");
            return basicResult;
        }

        if(!dbDeliveryAddress.getMemberId().equals(loginMember.getId())){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该收货地址不是你的，不允许删除");
            return basicResult;
        }

        deliveryAddressService.deleteByPrimaryKey(param.getId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }


    @Operation(summary = "设为默认收货地址")
    @PostMapping(value = "/updateIsDefault")
    public BasicResult updateIsDefault(@RequestBody @Validated(value = {}) DeliveryAddress param){
        BasicResult basicResult = new BasicResult();
        Member loginMember = memberSessionManager.getSession(TokenUtil.getToken());

        DeliveryAddress dbDeliveryAddress = deliveryAddressService.selectByPrimaryKey(param.getId());
        if(dbDeliveryAddress == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该收货地址不存在");
            return basicResult;
        }

        if(!dbDeliveryAddress.getMemberId().equals(loginMember.getId())){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该收货地址不是你的，不允许修改");
            return basicResult;
        }

        DeliveryAddress updateDeliveryAddress = new DeliveryAddress();
        updateDeliveryAddress.setId(dbDeliveryAddress.getId());
        updateDeliveryAddress.setIsDefault(Quantity.INT_1);
        deliveryAddressService.updateByPrimaryKeySelective(updateDeliveryAddress);

        deliveryAddressService.updateIsDefaultExclusion(dbDeliveryAddress.getId(), dbDeliveryAddress.getMemberId());

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("设置成功");
        return basicResult;
    }
}
