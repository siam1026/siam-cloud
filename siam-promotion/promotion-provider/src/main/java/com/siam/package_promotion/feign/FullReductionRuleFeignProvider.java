package com.siam.package_promotion.feign;

import com.siam.package_common.entity.BasicResult;
import com.siam.package_promotion.entity.FullReductionRule;
import com.siam.package_promotion.service.FullReductionRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供给其他服务Feign客户端调用的用户模块相关接口
 */
@RestController
public class FullReductionRuleFeignProvider implements FullReductionRuleFeignApi {

    @Autowired
    private FullReductionRuleService fullReductionRuleService;

    public BasicResult insertSelective(FullReductionRule record){
        fullReductionRuleService.insertSelective(record);
        return BasicResult.success();
    }

    public BasicResult selectByPrimaryKey(@RequestParam("id") Integer id){
        return BasicResult.success(fullReductionRuleService.selectByPrimaryKey(id));
    }

    public BasicResult updateByPrimaryKeySelective(FullReductionRule record){
        fullReductionRuleService.updateByPrimaryKeySelective(record);
        return BasicResult.success();
    }

    @Override
    public BasicResult<List<FullReductionRule>> selectByShopId(FullReductionRule fullReductionRule) {
        return BasicResult.success(fullReductionRuleService.selectByShopId(fullReductionRule));
    }
}