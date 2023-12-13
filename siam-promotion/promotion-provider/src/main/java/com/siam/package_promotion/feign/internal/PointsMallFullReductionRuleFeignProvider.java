package com.siam.package_promotion.feign.internal;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.internal.PointsMallFullReductionRule;
import com.siam.package_promotion.service.internal.PointsMallFullReductionRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class PointsMallFullReductionRuleFeignProvider implements PointsMallFullReductionRuleFeignApi {

    @Autowired
    private PointsMallFullReductionRuleService pointsMallFullReductionRuleService;

    public BasicResult insertSelective(PointsMallFullReductionRule record){
        pointsMallFullReductionRuleService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(pointsMallFullReductionRuleService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(PointsMallFullReductionRule record){
        pointsMallFullReductionRuleService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }
}