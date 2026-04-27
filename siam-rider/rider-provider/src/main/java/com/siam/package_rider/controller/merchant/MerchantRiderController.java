package com.siam.package_rider.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_merchant.util.TokenUtil;
import com.siam.package_rider.entity.Rider;
import com.siam.package_rider.service.RiderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/rest/merchant/rider")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端商家自配送骑手信息模块相关接口", description = "MerchantRiderController")
public class MerchantRiderController {

    @Autowired
    private RiderService riderService;

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Operation(summary = "骑手信息列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) Rider rider, HttpServletRequest request){
        BasicData basicResult = new BasicData();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        rider.setShopId(loginMerchant.getShopId());
        Page<Rider> page = riderService.getList(rider.getPageNo(), rider.getPageSize(), rider);

        return BasicResult.success(page);
    }

    @Operation(summary = "骑手信息创建")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) Rider rider, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        rider.setShopId(loginMerchant.getShopId());
        rider.setCreateTime(new Date());
        rider.setUpdateTime(new Date());
        riderService.insertSelective(rider);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("创建成功");
        return basicResult;
    }


    @Operation(summary = "骑手信息修改")
    @PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) Rider rider, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        Rider dbRider = riderService.selectByPrimaryKey(rider.getId());
        if (dbRider == null){
            throw new StoneCustomerException("该骑手信息不存在");
        } else if (loginMerchant.getShopId() != dbRider.getShopId()){
            throw new StoneCustomerException("您没有权限操作该骑手信息");
        }

        rider.setUpdateTime(new Date());
        riderService.updateByPrimaryKeySelective(rider);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }


    @Operation(summary = "骑手信息删除")
    @DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) Rider rider){
        BasicResult basicResult = new BasicResult();

        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        for (Integer id : rider.getIds()) {
            Rider dbRider = riderService.selectByPrimaryKey(id);
            if (dbRider == null){
                throw new StoneCustomerException("该骑手信息不存在");
            } else if (loginMerchant.getShopId() != dbRider.getShopId()){
                throw new StoneCustomerException("您没有权限操作该骑手信息");
            }
            riderService.deleteByPrimaryKey(id);
        }
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
}
