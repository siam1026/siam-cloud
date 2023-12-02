package com.siam.package_goods.feign.internal;

import com.siam.package_goods.entity.internal.VipRechargeDenomination;
import com.siam.package_goods.service.internal.VipRechargeDenominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
@RequestMapping(value = "/rest/vipRechargeDenominationFeign")
public class VipRechargeDenominationFeign {

    @Autowired
    private VipRechargeDenominationService vipRechargeDenominationService;

    @RequestMapping(value = "/selectLastestPaid")
    VipRechargeDenomination selectByPrimaryKey(@RequestParam("memberId") Integer id){
        return vipRechargeDenominationService.selectByPrimaryKey(id);
    }
}