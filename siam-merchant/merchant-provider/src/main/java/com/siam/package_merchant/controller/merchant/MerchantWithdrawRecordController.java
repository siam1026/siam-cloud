package com.siam.package_merchant.controller.merchant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_merchant.entity.MerchantWithdrawRecord;
import com.siam.package_merchant.model.param.MerchantWithdrawRecordParam;
import com.siam.package_merchant.service.MerchantBillingRecordService;
import com.siam.package_merchant.service.MerchantWithdrawRecordService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/rest/merchant/merchantWithdrawRecord")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端商家提现记录模块相关接口", description = "MerchantWithdrawRecordController")
public class MerchantWithdrawRecordController {

    @Autowired
    private MerchantWithdrawRecordService merchantWithdrawRecordService;

    @Autowired
    private MerchantBillingRecordService merchantBillingRecordService;

    @Operation(summary = "商家提现记录列表")
    @PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) MerchantWithdrawRecordParam param) {
        Page<MerchantWithdrawRecord> page = merchantWithdrawRecordService.getListByPage(param);
        return BasicResult.success(page);
    }

    @Operation(summary = "商家提现记录-统计金额")
    @PostMapping(value = "/statisticalAmount")
    public BasicResult statisticalAmount(@RequestBody @Validated(value = {}) MerchantWithdrawRecordParam param) {
        Map<String, Object> map = merchantWithdrawRecordService.statisticalAmount(param);
        return BasicResult.success(map);
    }

    @Operation(summary = "新增商家提现记录")
    @PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) MerchantWithdrawRecordParam param) {
        merchantWithdrawRecordService.insert(param);
        return BasicResult.success();
    }



    /*@Operation(summary = "修改商家提现记录")@PutMapping(value = "/update")
    public BasicResult update(MerchantWithdrawRecord merchantWithdrawRecord, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        String token = request.getHeader("token");
        MerchantToken merchantToken = merchantTokenService.getLoginMerchantInfo(token);

        MerchantWithdrawRecord dbMerchantWithdrawRecord = merchantWithdrawRecordService.selectByPrimaryKey(merchantWithdrawRecord.getId());
        if(dbMerchantWithdrawRecord == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该商家提现记录不存在，修改失败");
            return basicResult;
        }
        if(merchantToken.getMerchantId() != dbMerchantWithdrawRecord.getMerchantId()){
            throw new StoneCustomerException("您没有权限操作该记录");
        }

        //自动计算平台手续费
        Setting setting = settingFeignApi.selectCurrent().getData();
        BigDecimal merchantWithdrawFee = setting.getMerchantWithdrawFee().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal platformFee = merchantWithdrawRecord.getWithdrawAmount().multiply(merchantWithdrawFee).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal actualAmount = merchantWithdrawRecord.getWithdrawAmount().subtract(platformFee).setScale(2, BigDecimal.ROUND_HALF_UP);

        merchantWithdrawRecord.setPlatformFee(platformFee);
        merchantWithdrawRecord.setActualAmount(actualAmount);
        merchantWithdrawRecord.setUpdateTime(new Date());
        merchantWithdrawRecordService.updateByPrimaryKeySelective(merchantWithdrawRecord);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }*/


    /*@Operation(summary = "删除商家提现记录")@DeleteMapping(value = "/delete")
    public BasicResult delete(int id, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        String token = request.getHeader("token");
        MerchantToken merchantToken = merchantTokenService.getLoginMerchantInfo(token);

        MerchantWithdrawRecord dbMerchantWithdrawRecord = merchantWithdrawRecordService.selectByPrimaryKey(id);
        if(dbMerchantWithdrawRecord == null){
            basicResult.setSuccess(false);
            basicResult.setCode(BasicResultCode.ERR);
            basicResult.setMessage("该商家提现记录不存在，删除失败");
            return basicResult;
        }
        if(merchantToken.getMerchantId() != dbMerchantWithdrawRecord.getMerchantId()){
            throw new StoneCustomerException("您没有权限操作该记录");
        }

        merchantWithdrawRecordService.deleteByPrimaryKey(id);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }*/
}