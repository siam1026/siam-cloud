package com.siam.package_user.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_user.entity.internal.VipRechargeDenomination;
import com.siam.package_user.service.internal.VipRechargeDenominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class VipRechargeDenominationFeignProvider implements VipRechargeDenominationFeignApi {

    @Autowired
    private VipRechargeDenominationService vipRechargeDenominationService;

    public BasicResult selectByPrimaryKey(@RequestParam("memberId") Integer id){
        return BasicResult.success(vipRechargeDenominationService.selectByPrimaryKey(id));
    }
}