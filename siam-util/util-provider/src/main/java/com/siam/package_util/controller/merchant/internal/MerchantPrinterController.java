package com.siam.package_util.controller.merchant.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siam.package_common.annoation.MerchantPermission;
import com.siam.package_util.service.internal.PrinterService;
import com.siam.package_merchant.auth.cache.MerchantSessionManager;
import com.siam.package_common.entity.BasicData;
import com.siam.package_common.entity.BasicResult;
import com.siam.package_common.constant.BasicResultCode;
import com.siam.package_common.exception.StoneCustomerException;
import com.siam.package_util.entity.internal.Printer;
import com.siam.package_merchant.entity.Merchant;
import com.siam.package_user.util.TokenUtil;
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

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/rest/merchant/printer")
@Transactional(rollbackFor = Exception.class)
@Tag(name = "商家端商家打印机信息模块相关接口", description = "MerchantPrinterController")
public class MerchantPrinterController {

    @Autowired
    private MerchantSessionManager merchantSessionManager;

    @Autowired
    private PrinterService printerService;

//    @Autowired
//    private MerchantService merchantService;

    @Operation(summary = "打印机信息列表")@PostMapping(value = "/list")
    public BasicResult list(@RequestBody @Validated(value = {}) Printer printer, HttpServletRequest request){
        BasicData basicResult = new BasicData();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        printer.setShopId(loginMerchant.getShopId());
        Page<Printer> page = printerService.getList(printer.getPageNo(), printer.getPageSize(), printer);

        return BasicResult.success(page);
    }

    @Operation(summary = "打印机信息创建")@PostMapping(value = "/insert")
    public BasicResult insert(@RequestBody @Validated(value = {}) Printer printer, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        printer.setShopId(loginMerchant.getShopId());
        printer.setCreateTime(new Date());
        printer.setUpdateTime(new Date());
        printerService.insertSelective(printer);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("创建成功");
        return basicResult;
    }

    @MerchantPermission
    @Operation(summary = "打印机信息修改")@PutMapping(value = "/update")
    public BasicResult update(@RequestBody @Validated(value = {}) Printer printer, HttpServletRequest request){
        BasicResult basicResult = new BasicResult();

        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        Printer dbPrinter = printerService.selectByPrimaryKey(printer.getId());
        if (dbPrinter == null){
            throw new StoneCustomerException("该打印机信息不存在");
        } else if (loginMerchant.getShopId() != dbPrinter.getShopId()){
            throw new StoneCustomerException("您没有权限操作该打印机信息");
        }

        printer.setUpdateTime(new Date());
        printerService.updateByPrimaryKeySelective(printer);

        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("修改成功");
        return basicResult;
    }


    @MerchantPermission
    @Operation(summary = "打印机信息删除")@DeleteMapping(value = "/delete")
    public BasicResult delete(@RequestBody @Validated(value = {}) Printer param){
        BasicResult basicResult = new BasicResult();
        //获取当前登录用户绑定的门店编号
        Merchant loginMerchant = merchantSessionManager.getSession(TokenUtil.getToken());

        for (Integer id : param.getIds()) {
            Printer dbPrinter = printerService.selectByPrimaryKey(id);
            if (dbPrinter == null){
                throw new StoneCustomerException("该打印机信息不存在");
            } else if (loginMerchant.getShopId() != dbPrinter.getShopId()){
                throw new StoneCustomerException("您没有权限操作该打印机信息");
            }
            printerService.deleteByPrimaryKey(id);
        }
        basicResult.setSuccess(true);
        basicResult.setCode(BasicResultCode.SUCCESS);
        basicResult.setMessage("删除成功");
        return basicResult;
    }
}